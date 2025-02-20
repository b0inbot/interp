package boinsoft.interp.examples;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
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

class CachingSupplier implements Supplier<Object> {
  private Object obj_;
  private final Supplier<Object> source_;

  public CachingSupplier(Supplier<Object> source) {
    source_ = source;
    obj_ = null;
  }

  public Object get() {
    if (obj_ == null) {
      obj_ = source_.get();
    }
    return obj_;
  }
}

/**
 * An example of a global 'system' module that we can use to import other modules and interact with
 * the interp system.
 *
 * <p>This is useful since 'import' is an disallowed keyword in skylark but i really want it.
 */
@StarlarkBuiltin(name = "insys", category = "interp", doc = "")
public class InterpSystem {
  // TODO: check if we need to syncronize on the map
  private final Map<String, CachingSupplier> modules_;
  private final Module module_;
  private final StarlarkThread thread_;

  // Variables bound by load in one REPL chunk are visible in the next.
  private static final FileOptions OPTIONS =
      FileOptions.DEFAULT.toBuilder().allowToplevelRebinding(true).loadBindsGlobally(true).build();

  public InterpSystem() {
    modules_ = new HashMap<>();

    Map<String, Object> predeclared = new HashMap<>();
    predeclared.put("insys", this);

    module_ = Module.withPredeclared(StarlarkSemantics.DEFAULT, /* predeclared= */ predeclared);

    Mutability mu = Mutability.create("interp");
    thread_ = StarlarkThread.createTransient(mu, StarlarkSemantics.DEFAULT);
    thread_.setPrintHandler((th, msg) -> System.out.println(msg));
  }

  @StarlarkMethod(
      name = "Import",
      parameters = {@Param(name = "name")})
  public Object importMod(String name) {
    if (modules_.containsKey(name)) {
      return modules_.get(name).get();
    }
    return null;
  }

  /** Execute a Starlark file. */
  public int execute(ParserInput input) {
    try {
      Starlark.execFile(input, OPTIONS, module_, thread_);
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

  public void registerModule(String name, Supplier<Object> factory) {
    // TODO: check for duplicates
    modules_.put(name, new CachingSupplier(factory));
  }
}
