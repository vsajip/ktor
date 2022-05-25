description = "Server side Resources feature"

plugins {
    kotlin("plugin.serialization")
}

kotlin.sourceSets {
    jvmAndNixMain {
        dependencies {
            api(project(":ktor-shared:ktor-resources"))
            api(libs.kotlinx.serialization.core)
        }
    }
}
