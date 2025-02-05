# interp

this is not a custom java skylark implementation. for now its just a mirroring of
the skylark java source code into a place where i can rely on its API stability.

use at your own peril.

 * code is licensed the same as bazel (apache-2.0) to keep it simple.
 * code is based on the skylark codebase from bazel 8.0.1


What is missing?

 * tests from upstream bazel repo
 * cpu profiler
 * Some possibly unused auto value plugins
 * everything under com.google.devtools.
