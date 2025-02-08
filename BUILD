load("@rules_license//rules:license.bzl", "license")

exports_files([
    "LICENSE",
    "MODULE.bazel.lock",
])

license(
    name = "license-interp",
    package_name = "boinsoft/interp",
    copyright_notice = "Copyright © 2025 Boinsoft. All rights reserved.",
    license_kinds = [
        "@rules_license//licenses/spdx:Apache-2.0",
    ],
    license_text = "LICENSE",
    visibility = ["//examples:__subpackages__"],
)

license(
    name = "license-bazel",
    package_name = "bazelbuild/bazel",
    copyright_notice = "Copyright © 2014 The Bazel Authors. All rights reserved.",
    license_kinds = [
        "@rules_license//licenses/spdx:Apache-2.0",
    ],
    license_text = "LICENSE",
)

# for compat with BUILD files imported from the bazel repo
alias(
    name = "license",
    actual = ":license-bazel",
    visibility = ["//src:__subpackages__"],
)

alias(
    name = "format",
    actual = "//tools/format",
)
