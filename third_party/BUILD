load("@rules_java//java:defs.bzl", "java_import", "java_library", "java_plugin")
load("@rules_license//rules:license.bzl", "license")

package(default_visibility = ["//visibility:public"])

alias(
    name = "jsr305",
    actual = "@boinsoft_interp_maven//:com_google_code_findbugs_jsr305",
)

alias(
    name = "jcip_annotations",
    actual = "@boinsoft_interp_maven//:com_github_stephenc_jcip_jcip_annotations",
)

java_library(
    name = "flogger",
    applicable_licenses = [":flogger_license"],
    exports = [
        "@boinsoft_interp_maven//:com_google_flogger_flogger",
        "@boinsoft_interp_maven//:com_google_flogger_flogger_system_backend",
        "@boinsoft_interp_maven//:com_google_flogger_google_extensions",
    ],
)

license(
    name = "flogger_license",
    package_name = "flogger/0.5.1",
    license_kinds = [
        "@rules_license//licenses/spdx:Apache-2.0",
    ],
)

java_library(
    name = "error_prone_annotations",
    exports = [
        "@boinsoft_interp_maven//:com_google_errorprone_error_prone_annotations",
        "@boinsoft_interp_maven//:com_google_errorprone_error_prone_type_annotations",
    ],
)

java_library(
    name = "guava",
    applicable_licenses = [":guava_license"],
    exports = [
        ":error_prone_annotations",
        ":jcip_annotations",
        ":jsr305",
        "@boinsoft_interp_maven//:com_google_guava_guava",
    ],
)

license(
    name = "guava_license",
    package_name = "guava/31.1",
    license_kinds = [
        "@rules_license//licenses/spdx:Apache-2.0",
    ],
    license_text = "guava/LICENSE",
)

java_library(
    name = "truth",
    testonly = 1,
    exports = [
        "@boinsoft_interp_maven//:com_google_truth_extensions_truth_java8_extension",
        "@boinsoft_interp_maven//:com_google_truth_extensions_truth_proto_extension",
        "@boinsoft_interp_maven//:com_google_truth_truth",
    ],
)

alias(
    name = "compile_testing",
    testonly = 1,
    actual = "@boinsoft_interp_maven//:com_google_testing_compile_compile_testing",
)

java_plugin(
    name = "auto_value_plugin",
    processor_class = "com.google.auto.value.processor.AutoValueProcessor",
    deps = [
        ":apache_commons_collections",
        ":apache_velocity",
        ":auto_common",
        ":auto_service_lib",
        ":auto_value_lib",
        ":guava",
        ":tomcat_annotations_api",
    ],
)

alias(
    name = "apache_commons_collections",
    actual = "@boinsoft_interp_maven//:commons_collections_commons_collections",
)

java_library(
    name = "auto_service_lib",
    exports = [
        "@boinsoft_interp_maven//:com_google_auto_service_auto_service",
        "@boinsoft_interp_maven//:com_google_auto_service_auto_service_annotations",
    ],
)

alias(
    name = "auto_common",
    actual = "@boinsoft_interp_maven//:com_google_auto_auto_common",
)

alias(
    name = "apache_velocity",
    actual = "@boinsoft_interp_maven//:org_apache_velocity_velocity",
)

java_library(
    name = "auto_value",
    exported_plugins = [
        ":auto_value_plugin",
    ],
    exports = [
        ":auto_value_api",
        ":tomcat_annotations_api",
        "@boinsoft_interp_maven//:com_ryanharter_auto_value_auto_value_gson_runtime",
    ],
)

java_library(
    name = "auto_value_api",
    exports = [
        "@boinsoft_interp_maven//:com_google_auto_value_auto_value_annotations",
    ],
)

java_library(
    name = "auto_value_lib",
    exports = [
        "@boinsoft_interp_maven//:com_google_auto_value_auto_value",
        "@boinsoft_interp_maven//:com_google_auto_value_auto_value_annotations",
    ],
)

alias(
    name = "tomcat_annotations_api",
    actual = "@boinsoft_interp_maven//:org_apache_tomcat_tomcat_annotations_api",
)
