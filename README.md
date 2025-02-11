# interp

this is not a custom java starlark implementation. for now its just a mirroring of
the starlark java source code into a place where i can rely on its API stability.

use at your own peril.

 * code is licensed the same as bazel (apache-2.0) to keep it simple.
 * code is based on the starlark codebase from bazel 8.0.1

What is missing?

 * cpu profiler
 * Some possibly unused auto value plugins
 * everything under com.google.devtools.

## What works:

### Testing

`bazel test --config=starlark` will run everything under src/test/java/net/starlark

### The standalone repl

`bazel run --config=repl`

and

`bazel build --config=starlark`

### Example embeddings

`bazel run examples:example01 -- $PWD/examples/hello.sky`
`bazel run examples:example02 -- $PWD/examples/fetch.sky`
