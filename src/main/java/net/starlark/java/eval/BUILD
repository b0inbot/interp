# Description: Bazel's Starlark interpreter

load("@rules_java//java:defs.bzl", "java_library")

licenses(["notice"])  # Apache 2.0

filegroup(
    name = "srcs",
    srcs = glob(["**"]),
    visibility = ["//src/main/java/net/starlark/java:bazel"],
)

# The Starlark evaluator
java_library(
    name = "eval",
    srcs = [
        "BuiltinFunction.java",
        "CallUtils.java",
        "CpuProfiler.java",
        "Debug.java",
        "Dict.java",
        "Eval.java",
        "EvalException.java",
        "EvalUtils.java",
        "FlagGuardedValue.java",
        "FormatParser.java",
        "GuardedValue.java",
        "HasBinary.java",
        "ImmutableSingletonStarlarkList.java",
        "ImmutableStarlarkList.java",
        "JNI.java",
        "LazyImmutableStarlarkList.java",
        "MethodDescriptor.java",
        "MethodLibrary.java",
        "Module.java",
        "Mutability.java",
        "MutableStarlarkList.java",
        "NoneType.java",
        "ParamDescriptor.java",
        "Printer.java",
        "RangeList.java",
        "RegularImmutableStarlarkList.java",
        "RegularTuple.java",
        "Sequence.java",
        "SingletonTuple.java",
        "Starlark.java",
        "StarlarkCallable.java",
        "StarlarkFloat.java",
        "StarlarkFunction.java",
        "StarlarkIndexable.java",
        "StarlarkInt.java",
        "StarlarkIterable.java",
        "StarlarkList.java",
        "StarlarkSemantics.java",
        "StarlarkThread.java",
        "StarlarkValue.java",
        "StringModule.java",
        "Structure.java",
        "SymbolGenerator.java",
        "Tuple.java",
    ],
    visibility = ["//src/main/java/net/starlark/java:clients"],
    deps = [
        # Do not add Bazel or Google dependencies here!
        "//src/main/java/net/starlark/java/annot",
        "//src/main/java/net/starlark/java/spelling",
        "//src/main/java/net/starlark/java/syntax",
        "//third_party:error_prone_annotations",
        "//third_party:auto_value",
        "//third_party:flogger",
        "//third_party:guava",
        "//third_party:jsr305",
    ],
)
