# Examples

load("@rules_java//java:defs.bzl", "java_binary", "java_test")

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
    name = "runtime",
    srcs = ["Engine.java"],
    deps = [
        "//src/main/java/net/starlark/java/annot:annot_sans_processor",
        "//src/main/java/net/starlark/java/eval",
        "//src/main/java/net/starlark/java/syntax",
        "//third_party:guava",
        "//third_party:jsr305",
    ],
)

java_test(
    name = "runtime_tests",
    srcs = [
        "EngineTest.java",
        "RuntimeTests.java",
    ],
    test_class = "boinsoft.interp.runtime.RuntimeTests",
    deps = [
        ":runtime",
        "//third_party:guava",
        "//third_party:junit4",
        "//third_party:truth",
    ],
)
