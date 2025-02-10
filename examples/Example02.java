package boinsoft.interp.examples;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import net.starlark.java.annot.Param;
import net.starlark.java.annot.StarlarkBuiltin;
import net.starlark.java.annot.StarlarkMethod;
import net.starlark.java.eval.Dict;
import net.starlark.java.eval.EvalException;
import net.starlark.java.eval.Module;
import net.starlark.java.eval.Mutability;
import net.starlark.java.eval.Starlark;
import net.starlark.java.eval.StarlarkCallable;
import net.starlark.java.eval.StarlarkSemantics;
import net.starlark.java.eval.StarlarkThread;
import net.starlark.java.eval.StarlarkValue;
import net.starlark.java.eval.Tuple;
import net.starlark.java.syntax.FileOptions;
import net.starlark.java.syntax.ParserInput;
import net.starlark.java.syntax.SyntaxError;

class CompletableFutureWrapper<T> implements StarlarkValue {
  java.util.concurrent.CompletableFuture<T> fut_;

  public CompletableFutureWrapper(java.util.concurrent.CompletableFuture<T> fut) {
    fut_ = fut;
  }

  @StarlarkMethod(name = "Get")
  public void get() throws Exception {
    //TODO: Return some optional type
    fut_.get();
  }
}

class HttpClientWrapper implements StarlarkValue {
  private final HttpClient client_;

  public HttpClientWrapper(HttpClient client) {
    this.client_ = client;
  }

  ExecutorService executorService = Executors.newFixedThreadPool(4);

  @StarlarkMethod(
      name = "Get",
      parameters = {@Param(name = "url")})
  public void get(String url) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
    HttpResponse<String> response = client_.send(request, HttpResponse.BodyHandlers.ofString());
    System.out.println(response.statusCode());
  }

  @StarlarkMethod(
      name = "AsyncGet",
      parameters = {@Param(name = "url")})
  public CompletableFutureWrapper<String> asyncGet(String url)
      throws IOException, InterruptedException {
    return new CompletableFutureWrapper(
        CompletableFuture.supplyAsync(
            () -> {
              try {
                get(url);
              } catch (Exception exn) {
                return "";
              }
              return "CompletableFuture Result";
            },
            executorService));
  }
}

@StarlarkBuiltin(name = "http", category = "http", doc = "")
class HTTP {

  @StarlarkMethod(name = "Client")
  public HttpClientWrapper client() {
    return new HttpClientWrapper(
        HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build());
  }
}

@StarlarkBuiltin(name = "futures", category = "async", doc = "")
class Futures {
  @StarlarkMethod(
      name = "All",
      parameters = {@Param(name = "items")})
  public CompletableFutureWrapper<Void> all(List<CompletableFutureWrapper> items) throws Exception {
    return new CompletableFutureWrapper(
        CompletableFuture.allOf(
            items.stream()
                .map((x) -> x.fut_)
                .collect(Collectors.toList())
                .toArray(new CompletableFuture[items.size()])));
  }
}

@StarlarkBuiltin(name = "import", category = "import", doc = "")
class Import implements StarlarkCallable {

  public Object call(StarlarkThread thread, Tuple args, Dict<String, Object> kwargs) {
    if (args.get(0).equals("http/1.0.0")) {
      return new HTTP();
    }
    if (args.get(0).equals("futures/1.0.0")) {
      return new Futures();
    }
    return null;
  }

  public String getName() {
    return "import";
  }
}

public class Example02 {

  private static final FileOptions OPTIONS =
      FileOptions.DEFAULT.toBuilder().allowToplevelRebinding(true).loadBindsGlobally(true).build();

  private static final Module module;
  private static final StarlarkThread thread;

  static {
    Map<String, Object> predeclared = new HashMap<>();
    predeclared.put("importMod", new Import());
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
    String file = args[0];
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
