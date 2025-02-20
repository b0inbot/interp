package boinsoft.interp.examples;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import net.starlark.java.annot.Param;
import net.starlark.java.annot.StarlarkBuiltin;
import net.starlark.java.annot.StarlarkMethod;
import net.starlark.java.eval.StarlarkValue;
import net.starlark.java.syntax.ParserInput;

class CompletableFutureWrapper<T> implements StarlarkValue {
  java.util.concurrent.CompletableFuture<T> fut_;

  public CompletableFutureWrapper(java.util.concurrent.CompletableFuture<T> fut) {
    fut_ = fut;
  }

  @StarlarkMethod(name = "Get")
  public void get() throws Exception {
    // TODO: Return some optional type
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
                String threadName = Thread.currentThread().getName();
                System.out.println(String.format("[%s] GET %s", threadName, url));
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
class HTTP implements StarlarkValue {

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
class Futures implements StarlarkValue {
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

public class Example02 {
  public static void main(String[] args) {
    InterpSystem system = new InterpSystem();
    system.registerModule("http/1.0.0", () -> new HTTP());
    system.registerModule("futures/1.0.0", () -> new Futures());

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
