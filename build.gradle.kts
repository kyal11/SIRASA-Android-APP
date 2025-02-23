plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    alias(libs.plugins.compose.compiler) apply false
}