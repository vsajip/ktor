/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.utils.io

import io.ktor.io.*
import java.nio.*

internal class SourceByteReadChannelJvm(source: BufferedSource) : SourceByteReadChannel(source) {

    override fun readAvailable(min: Int, block: (ByteBuffer) -> Unit): Int {
        TODO()
    }

    override suspend fun readAvailable(dst: ByteBuffer): Int {
        TODO()
    }

    override suspend fun readFully(dst: ByteBuffer): Int {
        TODO()
    }

    override suspend fun read(min: Int, consumer: (ByteBuffer) -> Unit) {
        TODO()
    }
}
