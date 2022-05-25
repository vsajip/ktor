/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package server

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.services.*
import org.gradle.api.tasks.*
import org.gradle.api.tasks.testing.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.*
import org.jetbrains.kotlin.gradle.tasks.*
import java.io.*
import java.net.*

abstract class KtorTestWorker : BuildService<KtorTestWorker.Params>, AutoCloseable {
    open class Params : BuildServiceParameters

    private val server = startServer()

    override fun close() {
        server.close()
    }
}

class TestServerPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.gradle.sharedServices.registerIfAbsent("KtorTestWorker", KtorTestWorker::class.java) {
            KtorTestWorker.Params()
        }

        target.tasks.configureEach {
            if (!isTest) return@configureEach
        }
    }
}

val Task.isTest: Boolean
    get() = this.name.endsWith("Test")
