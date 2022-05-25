description = "Client side Resources feature"

plugins {
    kotlin("plugin.serialization")
}

kotlin.sourceSets {
    commonMain {
        dependencies {
            api(project(":ktor-shared:ktor-resources"))
            api(libs.kotlinx.serialization.core)
        }
    }
}
