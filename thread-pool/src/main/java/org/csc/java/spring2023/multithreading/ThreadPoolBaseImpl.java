package org.csc.java.spring2023.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Supplier;

public class ThreadPoolBaseImpl extends ThreadPoolBase {

  private final BlockingQueue<ComposableFutureImpl<?>> taskQueue;

  private final List<Thread> threads;
  private volatile boolean shutDown;

  ThreadPoolBaseImpl(int numberOfThreads) {
    taskQueue = new LinkedBlockingDeque<>();
    threads = new ArrayList<>(numberOfThreads);
    shutDown = false;
    for (int i = 0; i < numberOfThreads; i++) {
      threads.add(createWorkerThread());
      threads.get(i).start();
    }
  }

  @Override
  public <T> ComposableFuture<T> invoke(Supplier<? extends T> action) {
    if (shutDown) {
      throw new IllegalStateException("ThreadPool is closed");
    }
    System.err.println("Got new function");
    ComposableFutureImpl<T> composableFuture = new ComposableFutureImpl<>(action);
    taskQueue.add(composableFuture);
    return composableFuture;
  }

  @Override
  public void shutdown() {
    shutDown = true;
    threads.forEach(Thread::interrupt);
    System.err.println("Interrupted everything");
  }

  @Override
  public void awaitFullShutdown() throws InterruptedException {
    if (!shutDown) {
      throw new IllegalStateException("Pool can't wait without being shutDown");
    }
    for (var t : threads) {
      t.join();
    }
  }

  @Override
  public List<Thread> getThreads() {
    return threads;
  }

  @Override
  protected Runnable createWorker() {
    return () -> {
      try {
        while (!shutDown) {
          ComposableFutureImpl<?> task = taskQueue.take();
          task.runAction();
        }
      } catch (InterruptedException ignored) {
        return;
        // unnecessary return in the end to show reviewdog, that I don't ignore the error
      }
    };
  }
}
