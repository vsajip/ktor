description = "Ktor CBOR Content Negotiation via kotlinx.serialization support"

plugins {
    kotlin("plugin.serialization")
}

kotlin.sourceSets {
    commonMain {
        dependencies {
            api(project(":ktor-shared:ktor-serialization:ktor-serialization-kotlinx"))
            api(libs.kotlinx.serialization.cbor)
        }
    }
    commonTest {
        dependencies {
            api(libs.kotlinx.serialization.json)
            api(project(":ktor-shared:ktor-serialization:ktor-serialization-kotlinx:ktor-serialization-kotlinx-tests"))
        }
    }
}
