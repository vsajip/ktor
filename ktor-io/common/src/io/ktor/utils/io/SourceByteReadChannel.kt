/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.utils.io

import io.ktor.io.*
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*

internal abstract class SourceByteReadChannel(val source: BufferedSource): ByteReadChannel {
    override val availableForRead: Int
        get() = source.peek().readCapacity()

    override val isClosedForRead: Boolean
        get() = TODO("Not yet implemented")

    override val isClosedForWrite: Boolean
        get() = true

    override val closedCause: Throwable?
        get() = source.cancelCause

    override val totalBytesRead: Long
        get() = TODO("Not yet implemented")

    override suspend fun readAvailable(dst: ByteArray, offset: Int, length: Int): Int {
        source.awaitContent()

        val buffer = source.peek()
        return buffer.read(dst, offset, length)
    }

    override suspend fun readAvailable(dst: ChunkBuffer): Int {
        val buffer = source.read()
        TODO("Not yet implemented")
    }

    override suspend fun readFully(dst: ByteArray, offset: Int, length: Int) {
        var size = 0
        while (size < length) {
            awaitContent()
            val buffer = source.read()
            size += buffer.read(dst, offset + size, length - size)
        }
    }

    override suspend fun readFully(dst: ChunkBuffer, n: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun readPacket(size: Int): ByteReadPacket {
        TODO()
    }

    override suspend fun readRemaining(limit: Long): ByteReadPacket {
        TODO("Not yet implemented")
    }

    override suspend fun readLong(): Long = source.readLong()

    override suspend fun readInt(): Int = source.readInt()

    override suspend fun readShort(): Short = source.readShort()

    override suspend fun readByte(): Byte = source.readByte()

    override suspend fun readBoolean(): Boolean = source.readBoolean()

    override suspend fun readDouble(): Double = source.readDouble()

    override suspend fun readFloat(): Float = source.readFloat()

    override suspend fun <A : Appendable> readUTF8LineTo(out: A, limit: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun readUTF8Line(limit: Int): String? {
        TODO("Not yet implemented")
    }

    override fun cancel(cause: Throwable?): Boolean {
        source.cancel(cause ?: Exception())
        return TODO()
    }

    override suspend fun discard(max: Long): Long {
        var remaining = max
        while (true) {
            val buffer = source.peek()

            if (buffer.readCapacity() > remaining) {
                remaining -= buffer.readCapacity()
                source.read().release()
            } else {
                buffer.readIndex += remaining.toInt()
                return max - remaining
            }

            if (!source.awaitContent()) {
                return max - remaining
            }
        }
    }

    override suspend fun awaitContent() {
        source.awaitContent()
    }

    override suspend fun peekTo(
        destination: Memory,
        destinationOffset: Long,
        offset: Long,
        min: Long,
        max: Long
    ): Long {
        TODO("Not yet implemented")
    }
}
