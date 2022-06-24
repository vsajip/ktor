package io.ktor.utils.io.internal

import io.ktor.utils.io.pool.*
import java.nio.*

internal val BUFFER_SIZE = getIOIntProperty("BufferSize", 4096)
private val BUFFER_POOL_SIZE = getIOIntProperty("BufferPoolSize", 2048)

// ------------- standard shared pool objects -------------

internal val BufferPool: ObjectPool<ByteBuffer> = DirectByteBufferPool(BUFFER_POOL_SIZE, BUFFER_SIZE)
