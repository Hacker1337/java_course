package org.csc.java.spring2023.multithreading;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;

public class ComposableFutureImpl<T> implements ComposableFuture<T> {

  private final Supplier<? extends T> action;
  private volatile T result;
  private volatile Status status;
  private Throwable exception;
  private final ReentrantLock waitingResultLock;

  void runAction() {
    try {
      result = action.get();
    } catch (Exception e) {
      exception = e;
      status = Status.FINISHED_WITH_EXCEPTION;
      synchronized (waitingResultLock) {
        waitingResultLock.notifyAll();
      }
      throw e;
    }
    status = Status.FINISHED;
    synchronized (waitingResultLock) {
      waitingResultLock.notifyAll();
    }
  }

  public ComposableFutureImpl(Supplier<? extends T> action) {
    this.action = action;
    this.status = Status.NOT_FINISHED;
    waitingResultLock = new ReentrantLock();
  }


  @Override
  public Status getStatus() {
    return status;
  }

  @Override
  public synchronized T get() throws ExecutionException, InterruptedException {
    while (status == Status.NOT_FINISHED) {
      System.err.println("Begun to wait");
      synchronized (waitingResultLock) {
        waitingResultLock.wait();
      }
      System.err.println("Finished waiting");
    }
    if (status == Status.FINISHED) {
      return result;
    } else if (status == Status.FINISHED_WITH_EXCEPTION) {
      throw new ExecutionException(this.exception);
    }
    throw new AssertionError("Reached unreachable code");
  }

  @Override
  public T getIfReady() throws ExecutionException {
    if (status == Status.FINISHED) {
      return result;
    } else if (status == Status.FINISHED_WITH_EXCEPTION) {
      throw new ExecutionException(this.exception);
    } else {
      throw new IllegalStateException();
    }
  }

  @Override
  public <U> ComposableFuture<U> thenApply(Function<? super T, ? extends U> mapper) {
    Supplier<U> supplier = () -> {
      while (getStatus() == Status.NOT_FINISHED) {
        synchronized (waitingResultLock) {
          try {
            waitingResultLock.wait();
          } catch (InterruptedException e) {
            break;
          }
        }
      }
      try {
        return mapper.apply(getIfReady());
      } catch (ExecutionException e) {
        throw new RuntimeException(e);
        // не правильно, конечно, но не понимаю, как выкинуть оригинальную ошибку из лямбды
      }
    };
    return new ComposableFutureImpl<>(supplier);
  }
}
