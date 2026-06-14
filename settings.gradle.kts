/*
 * Multi-module build for mini-idp, a small standalone identity provider.
 *
 *   core   - identity model + crypto + token issuance/verification (no HTTP, no I/O transport).
 *   server - the HTTP daemon (JDK HttpServer on loopback) that exposes the OAuth/JWKS/admin
 *            API and serves the OpenAPI spec + Swagger UI; depends on core.
 *
 * mini-idp is a sibling service to mini-kms. It does NOT depend on mini-kms; it only mirrors
 * its conventions and issues tokens whose claims map cleanly onto the mini-kms identity model.
 */

plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs.
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "mini-idp"

include("core")
include("server")
