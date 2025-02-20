# Examples

load("@rules_java//java:defs.bzl", "java_binary")

package(
    default_applicable_licenses = ["//:license-interp"],
    default_visibility = ["//:__subpackages__"],
)

filegroup(
    name = "srcs",
    srcs = glob(["**"]),
    visibility = ["//:__subpackages__"],
)

java_library(
    name = "common",
    srcs = [
        "InterpSystem.java",
    ],
    deps = [
        "//src/main/java/net/starlark/java/annot:annot_sans_processor",
        "//src/main/java/net/starlark/java/eval",
        "//src/main/java/net/starlark/java/syntax",
        "//third_party:guava",
        "//third_party:jsr305",
    ],
)

java_binary(
    name = "example01",
    srcs = ["Example01.java"],
    jvm_flags = [
        # quiet warnings from com.google.protobuf.UnsafeUtil,
        # see: https://github.com/google/protobuf/issues/3781
        # and: https://github.com/bazelbuild/bazel/issues/5599
        "--add-opens=java.base/java.nio=ALL-UNNAMED",
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        # ... but only on JDK >= 9
        "-XX:+IgnoreUnrecognizedVMOptions",
    ],
    main_class = "boinsoft.interp.examples.Example01",
    deps = [
        ":common",
        "//src/main/java/net/starlark/java/annot:annot_sans_processor",
        "//src/main/java/net/starlark/java/eval",
        "//src/main/java/net/starlark/java/syntax",
        "//third_party:guava",
        "//third_party:jsr305",
    ],
)

java_binary(
    name = "example02",
    srcs = ["Example02.java"],
    jvm_flags = [
        # quiet warnings from com.google.protobuf.UnsafeUtil,
        # see: https://github.com/google/protobuf/issues/3781
        # and: https://github.com/bazelbuild/bazel/issues/5599
        "--add-opens=java.base/java.nio=ALL-UNNAMED",
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        # ... but only on JDK >= 9
        "-XX:+IgnoreUnrecognizedVMOptions",
    ],
    main_class = "boinsoft.interp.examples.Example02",
    deps = [
        ":common",
        "//src/main/java/net/starlark/java/annot:annot_sans_processor",
        "//src/main/java/net/starlark/java/eval",
        "//src/main/java/net/starlark/java/syntax",
        "//third_party:guava",
        "//third_party:jsr305",
    ],
)
