package boinsoft.interp.examples;

import java.io.IOException;
import net.starlark.java.annot.Param;
import net.starlark.java.annot.StarlarkBuiltin;
import net.starlark.java.annot.StarlarkMethod;
import net.starlark.java.eval.StarlarkValue;
import net.starlark.java.syntax.ParserInput;

@StarlarkBuiltin(name = "io", category = "io", doc = "")
class IO implements StarlarkValue {

  @StarlarkMethod(
      name = "cprint",
      parameters = {@Param(name = "x")})
  public void cprint(Object x) {
    System.out.println(x);
  }
}

public class Example01 {

  public static void main(String[] args) {
    InterpSystem system = new InterpSystem();
    system.registerModule("io", () -> new IO());

    String file = args[0];
    int exit = 0;
    try {
      exit = system.execute(ParserInput.readFile(file));
    } catch (IOException e) {
      // This results in such lame error messages as:
      // "Error reading a.star: java.nio.file.NoSuchFileException: a.star"
      System.err.format("Error reading %s: %s\n", file, e);
      exit = 1;
    }
    System.exit(exit);
  }
}
