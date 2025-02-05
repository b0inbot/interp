package boinsoft.interp.examples;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.starlark.java.annot.Param;
import net.starlark.java.annot.StarlarkBuiltin;
import net.starlark.java.annot.StarlarkMethod;
import net.starlark.java.eval.EvalException;
import net.starlark.java.eval.Module;
import net.starlark.java.eval.Mutability;
import net.starlark.java.eval.Starlark;
import net.starlark.java.eval.StarlarkSemantics;
import net.starlark.java.eval.StarlarkThread;
import net.starlark.java.syntax.FileOptions;
import net.starlark.java.syntax.ParserInput;
import net.starlark.java.syntax.SyntaxError;

@StarlarkBuiltin(name = "io", category = "io", doc = "")
class IO {

  @StarlarkMethod(
      name = "cprint",
      parameters = {@Param(name = "x")})
  public void cprint(Object x) {
    System.out.println(x);
  }
}

public class Example01 {

  // Variables bound by load in one REPL chunk are visible in the next.
  private static final FileOptions OPTIONS =
      FileOptions.DEFAULT.toBuilder().allowToplevelRebinding(true).loadBindsGlobally(true).build();

  private static final Module module;
  private static final StarlarkThread thread;

  static {
    Map<String, Object> predeclared = new HashMap<>();
    predeclared.put("io", new IO());
    module = Module.withPredeclared(StarlarkSemantics.DEFAULT, /* predeclared= */ predeclared);

    Mutability mu = Mutability.create("interpreter");
    thread = StarlarkThread.createTransient(mu, StarlarkSemantics.DEFAULT);
    thread.setPrintHandler((th, msg) -> System.out.println(msg));
  }

  /** Execute a Starlark file. */
  private static int execute(ParserInput input) {
    try {
      Starlark.execFile(input, OPTIONS, module, thread);
      return 0;
    } catch (SyntaxError.Exception ex) {
      for (SyntaxError error : ex.errors()) {
        System.err.println(error);
      }
      return 1;
    } catch (EvalException ex) {
      System.err.println(ex.getMessageWithStack());
      return 1;
    } catch (InterruptedException e) {
      System.err.println("Interrupted");
      return 1;
    }
  }

  public static void main(String[] args) {
    String file = args[1];
    int exit = 0;
    try {
      exit = execute(ParserInput.readFile(file));
    } catch (IOException e) {
      // This results in such lame error messages as:
      // "Error reading a.star: java.nio.file.NoSuchFileException: a.star"
      System.err.format("Error reading %s: %s\n", file, e);
      exit = 1;
    }
    System.exit(exit);
  }
}
