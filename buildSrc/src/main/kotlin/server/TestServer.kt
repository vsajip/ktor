/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package server

import ch.qos.logback.classic.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import org.slf4j.*
import java.io.*
import java.util.concurrent.*
import server.tests.*

const val DEFAULT_PORT: Int = 8080
const val DEFAULT_TLS_PORT: Int = 8089
const val HTTP_PROXY_PORT: Int = 8082

val TEST_SERVER: String = "http://127.0.0.1:8080"

internal fun startServer(): Closeable {
    val logger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME) as ch.qos.logback.classic.Logger
    logger.level = Level.ERROR

    val scope = CloseableGroup()
    try {
        val tcpServer = TestTcpServer(HTTP_PROXY_PORT, ::tcpServerHandler)
        scope.use(tcpServer)

        val server = embeddedServer(CIO, DEFAULT_PORT) {
            tests()
        }.start()

        scope.use(Closeable { server.stop(0L, 0L, TimeUnit.MILLISECONDS) })

        val tlsServer = setupTLSServer()
        tlsServer.start()
        scope.use(Closeable { tlsServer.stop(0L, 0L, TimeUnit.MILLISECONDS) })

        Thread.sleep(1000)
    } catch (cause: Throwable) {
        scope.close()
    }

    return scope
}

private fun setupTLSServer(): ApplicationEngine {
    val file = File.createTempFile("server", "certificate")
    val testKeyStore = generateCertificate(file)
    val tlsServer = embeddedServer(
        Jetty,
        applicationEngineEnvironment {
            sslConnector(
                testKeyStore,
                "mykey",
                { "changeit".toCharArray() },
                { "changeit".toCharArray() },
                {
                    this.port = DEFAULT_TLS_PORT
                    this.keyStorePath = file
                }
            )

            module {
                tlsTests()
            }
        }
    )

    return tlsServer
}
