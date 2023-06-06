package org.csc.java.spring2023;

import static org.csc.java.spring2023.Token.DECREASE;
import static org.csc.java.spring2023.Token.INCREASE;
import static org.csc.java.spring2023.Token.INPUT;
import static org.csc.java.spring2023.Token.LEFT_BRACKET;
import static org.csc.java.spring2023.Token.NEXT;
import static org.csc.java.spring2023.Token.OUTPUT;
import static org.csc.java.spring2023.Token.PREVIOUS;
import static org.csc.java.spring2023.Token.RIGHT_BRACKET;
import static org.csc.java.spring2023.Token.UNRECOGNIZED_TOKEN;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import org.csc.java.spring2023.lint.Linter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BrainfuckTest {

  @Test
  void lexer_smoke() {
    Lexer lexer = Interpreter.createLexer();

    assertNotNull(lexer);

    Token[] tokens = lexer.tokenize("+-[]><.,!");
    Token[] expectedTokens = new Token[]{
        INCREASE,
        DECREASE,
        LEFT_BRACKET,
        RIGHT_BRACKET,
        NEXT,
        PREVIOUS,
        OUTPUT,
        INPUT,
        UNRECOGNIZED_TOKEN
    };

    assertArrayEquals(expectedTokens, tokens);
  }

  @Test
  void lexer_emptyProgram() {
    Lexer lexer = Interpreter.createLexer();

    assertArrayEquals(new Token[]{}, lexer.tokenize(""));
  }

  @Test
  void linter_smoke() {
    // This program has incorrect commands:
    // ? at position 0
    // * at position 4
    // $ at position 6
    String programText = "?++[*]$-";

    Lexer lexer = Interpreter.createLexer();
    assertNotNull(lexer);

    Linter linter = Interpreter.createLinter();
    assertNotNull(linter);

    Token[] tokens = lexer.tokenize(programText);
    List<LintProblem> problems = linter.lint(tokens, programText);

    assertEquals(3, problems.size());

    List<LintProblem> problemsSortedByPosition = problems.stream()
        .sorted(Comparator.comparing(LintProblem::getPosition))
        .toList();

    assertEquals(0, problemsSortedByPosition.get(0).getPosition());
    assertEquals(4, problemsSortedByPosition.get(1).getPosition());
    assertEquals(6, problemsSortedByPosition.get(2).getPosition());
  }

  @Test
  void linter_reports_error_for_every_unclosed_bracket() {
    // This program has two un-opened brackets and to un-closed brackets,
    // so linter should report 4 problems
    String programText = "]][[";

    Lexer lexer = Interpreter.createLexer();
    assertNotNull(lexer);

    Linter linter = Interpreter.createLinter();
    assertNotNull(linter);

    Token[] tokens = lexer.tokenize(programText);
    List<LintProblem> problems = linter.lint(tokens, programText);

    assertEquals(4, problems.size());

    List<LintProblem> problemsSortedByPosition = problems.stream()
        .sorted(Comparator.comparing(LintProblem::getPosition))
        .toList();

    assertEquals(0, problemsSortedByPosition.get(0).getPosition());
    assertEquals(1, problemsSortedByPosition.get(1).getPosition());
    assertEquals(2, problemsSortedByPosition.get(2).getPosition());
    assertEquals(3, problemsSortedByPosition.get(3).getPosition());
  }

  @Test
  void memory_smoke() {
    Memory memory = Interpreter.createMemory(42);
    assertNotNull(memory);

    assertEquals(42, memory.getSize());

    memory.shiftLeft();
    assertThrows(MemoryAccessException.class, memory::getByte);

    memory.shiftRight();
    memory.setByte((byte) 67);

    memory.shiftRight();
    memory.shiftLeft();

    assertEquals(67, memory.getByte());
  }

  @Nested
  class Basics {

    @BeforeEach
    void checkParser() {
      assertNotNull(Interpreter.createParser());
    }

    @Test
    void empty_program_works() {
      TestIOContext testContext = new TestIOContext();

      assertDoesNotThrow(() -> Interpreter.run(testContext, ""),
          "Empty string is a valid Brainfuck program, it should be correctly executed");

      assertArrayEquals(new byte[]{}, testContext.getOutput());
    }

    @Test
    void default_memory_is_zero() {
      TestIOContext testContext = new TestIOContext();

      Interpreter.run(testContext, ".");

      assertArrayEquals(new byte[]{0}, testContext.getOutput());
    }

    @Test
    void memory_increases_and_decreases() {
      TestIOContext testContext = new TestIOContext();

      Interpreter.run(testContext, ".+.+.-.-.");

      assertArrayEquals(new byte[]{0, 1, 2, 1, 0}, testContext.getOutput());
    }

    @Test
    void moving_between_memory_cells() {
      TestIOContext testContext = new TestIOContext();

      String program =
          ".>.<"        // print memory[0] and memory[1]
              + "+"     // memory[0]++
              + ".>.<"  // print memory[0] and memory[1]
              + ">+<"   // memory[1]++
              + ".>.<"; // print memory[0] and memory[1]

      Interpreter.run(testContext, program);

      assertArrayEquals(new byte[]{0, 0, 1, 0, 1, 1}, testContext.getOutput());
    }

    @Test
    void read_increase_print() {
      TestIOContext testContext = new TestIOContext(new byte[]{0});

      Interpreter.run(testContext, ",+.");

      assertArrayEquals(new byte[]{1}, testContext.getOutput());
    }
  }

  @Nested
  class MultiplyByTwo {

    // this program reads a positive number and multiplies it by two
    private final static String MULTIPLY_BY_TWO =
        ","           // read input to memory[0]
            + "["     // while memory[0] != 0 {
            + "-"     //  memory[0]--
            + ">++<"  //  memory[1] += 2
            + "]"     // }
            + ">.";   // print memory[1]

    @BeforeEach
    void checkParser() {
      assertNotNull(Interpreter.createParser());
    }

    @Test
    void zero_by_two_is_zero() {
      TestIOContext testContext = new TestIOContext(new byte[]{0});

      Interpreter.run(testContext, MULTIPLY_BY_TWO);

      assertArrayEquals(new byte[]{0}, testContext.getOutput());
    }

    @Test
    void one_by_two_is_two() {
      TestIOContext testContext = new TestIOContext(new byte[]{1});

      Interpreter.run(testContext, MULTIPLY_BY_TWO);

      assertArrayEquals(new byte[]{2}, testContext.getOutput());
    }

    @Test
    void five_by_two_is_ten() {
      TestIOContext testContext = new TestIOContext(new byte[]{5});

      Interpreter.run(testContext, MULTIPLY_BY_TWO);

      assertArrayEquals(new byte[]{10}, testContext.getOutput());
    }
  }

  @Test
  void interpreter_smoke() {
    assertNotNull(Interpreter.createParser());
    String readThreeBytesAndPrintThem = ",>,>,<<.>.>.";

    TestIOContext testContext = new TestIOContext("lol");

    Interpreter.run(testContext, readThreeBytesAndPrintThem);

    assertEquals("lol", testContext.getOutputAsString());
  }

  @Test
  void interpreter_hello_world() {
    assertNotNull(Interpreter.createParser());

    TestIOContext testContext = new TestIOContext();

    // See Wikipedia for the explanation of how this works: https://tinyurl.com/2p9r2pak
    String helloWorld =
        "++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.";

    Interpreter.run(testContext, helloWorld);

    assertEquals("Hello World!", testContext.getOutputAsString());
    assertThrows(IllegalArgumentException.class, () -> Interpreter.run(testContext, "+-?"));
  }

  @Test
  void interpreter_fails_on_reading_from_finished_stream() {
    TestIOContext emptyContext = new TestIOContext(new byte[]{});

    assertThrows(
        RuntimeException.class,
        () -> Interpreter.run(emptyContext, ","),
        "Reading from the empty context should throw an exception; brainfuck cannot express unsuccessful read."
    );
  }

  private static class TestIOContext extends IOContext {

    TestIOContext() {
      super(new ByteArrayInputStream(new byte[42]), new ByteArrayOutputStream());
    }

    TestIOContext(String input) {
      this(input.getBytes(StandardCharsets.UTF_8));
    }

    TestIOContext(byte[] input) {
      super(new ByteArrayInputStream(input), new ByteArrayOutputStream());
    }

    byte[] getOutput() {
      return ((ByteArrayOutputStream) out).toByteArray();
    }

    String getOutputAsString() {
      return out.toString();
    }
  }
}
