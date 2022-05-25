description = "Ktor Content Negotiation via kotlinx.serialization support"

plugins {
    kotlin("plugin.serialization")
}

kotlin.sourceSets {
    commonMain {
        dependencies {
            api(project(":ktor-shared:ktor-serialization"))
            api(libs.kotlinx.serialization.core)
        }
    }
}
