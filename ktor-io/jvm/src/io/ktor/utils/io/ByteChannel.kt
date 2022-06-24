package io.ktor.utils.io

import java.nio.*

/**
 * Creates channel for reading from the specified byte buffer.
 */
public fun ByteReadChannel(content: ByteBuffer): ByteReadChannel = TODO("$content")

/**
 * Creates buffered channel for asynchronous reading and writing of sequences of bytes.
 */
public actual fun ByteChannel(autoFlush: Boolean): ByteChannel = TODO()

/**
 * Creates channel for reading from the specified byte array.
 */
public actual fun ByteReadChannel(content: ByteArray, offset: Int, length: Int): ByteReadChannel = TODO()

/**
 * Creates buffered channel for asynchronous reading and writing of sequences of bytes using [close] function to close
 * channel.
 */
public fun ByteChannel(autoFlush: Boolean = false, exceptionMapper: (Throwable?) -> Throwable?): ByteChannel =
    TODO("$autoFlush, $exceptionMapper")
