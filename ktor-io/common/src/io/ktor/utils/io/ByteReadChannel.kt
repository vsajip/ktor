package io.ktor.utils.io

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*

/**
 * Channel for asynchronous reading of sequences of bytes.
 * This is a **single-reader channel**.
 *
 * Operations on this channel cannot be invoked concurrently.
 */
public expect interface ByteReadChannel {
    /**
     * Returns number of bytes that can be read without suspension. Read operations do no suspend and return
     * immediately when this number is at least the number of bytes requested for read.
     */
    public val availableForRead: Int

    /**
     * Returns `true` if the channel is closed and no remaining bytes are available for read.
     * It implies that [availableForRead] is zero.
     */
    public val isClosedForRead: Boolean

    public val isClosedForWrite: Boolean

    /**
     * A closure causes exception or `null` if closed successfully or not yet closed
     */
    public val closedCause: Throwable?

    /**
     * Number of bytes read from the channel.
     * It is not guaranteed to be atomic so could be updated in the middle of long-running read operation.
     */
    public val totalBytesRead: Long

    /**
     * Reads all available bytes to [dst] buffer and returns immediately or suspends if no bytes available
     * @return number of bytes were read or `-1` if the channel has been closed
     */
    public suspend fun readAvailable(dst: ByteArray, offset: Int, length: Int): Int

    /**
     * Reads all [length] bytes to [dst] buffer or fails if channel has been closed.
     * Suspends if not enough bytes available.
     */
    public suspend fun readFully(dst: ByteArray, offset: Int, length: Int)

    /**
     * Reads the specified amount of bytes and makes a byte packet from them. Fails if channel has been closed
     * and not enough bytes available.
     */
    public suspend fun readPacket(size: Int): ByteReadPacket

    /**
     * Reads up to [limit] bytes and makes a byte packet or until end of stream encountered.
     */
    public suspend fun readRemaining(limit: Long = Long.MAX_VALUE): ByteReadPacket

    /**
     * Reads a long number (suspending if not enough bytes available) or fails if channel has been closed
     * and not enough bytes.
     */
    public suspend fun readLong(): Long

    /**
     * Reads an int number (suspending if not enough bytes available) or fails if channel has been closed
     * and not enough bytes.
     */
    public suspend fun readInt(): Int

    /**
     * Reads a short number (suspending if not enough bytes available) or fails if channel has been closed
     * and not enough bytes.
     */
    public suspend fun readShort(): Short

    /**
     * Reads a byte (suspending if no bytes available yet) or fails if channel has been closed
     * and not enough bytes.
     */
    public suspend fun readByte(): Byte

    /**
     * Reads a boolean value (suspending if no bytes available yet) or fails if channel has been closed
     * and not enough bytes.
     */
    public suspend fun readBoolean(): Boolean

    /**
     * Reads double number (suspending if not enough bytes available) or fails if channel has been closed
     * and not enough bytes.
     */
    public suspend fun readDouble(): Double

    /**
     * Reads float number (suspending if not enough bytes available) or fails if channel has been closed
     * and not enough bytes.
     */
    public suspend fun readFloat(): Float

    /**
     * Reads a line of UTF-8 characters to the specified [out] buffer up to [limit] characters.
     * Supports both CR-LF and LF line endings. No line ending characters will be appended to [out] buffer.
     * Throws an exception if the specified [limit] has been exceeded.
     *
     * @return `true` if line has been read (possibly empty) or `false` if channel has been closed
     * and no characters were read.
     */
    public suspend fun <A : Appendable> readUTF8LineTo(out: A, limit: Int): Boolean

    /**
     * Reads a line of UTF-8 characters up to [limit] characters.
     * Supports both CR-LF and LF line endings.
     * Throws an exception if the specified [limit] has been exceeded.
     *
     * @return a line string with no line endings or `null` of channel has been closed
     * and no characters were read.
     */
    public suspend fun readUTF8Line(limit: Int): String?

    /**
     * Close channel with optional [cause] cancellation. Unlike [ByteWriteChannel.close] that could close channel
     * normally, cancel does always close with error so any operations on this channel will always fail
     * and all suspensions will be resumed with exception.
     *
     * Please note that if the channel has been provided by [reader] or [writer] then the corresponding owning
     * coroutine will be cancelled as well
     *
     * @see ByteWriteChannel.close
     */
    public fun cancel(cause: Throwable?): Boolean

    /**
     * Discard up to [max] bytes
     *
     * @return number of bytes were discarded
     */
    public suspend fun discard(max: Long): Long

    /**
     * Suspend until the channel has bytes to read or gets closed. Throws exception if the channel was closed with an error.
     */
    public suspend fun awaitContent()

    public companion object {
        public val Empty: ByteReadChannel
    }
}

/**
 * Reads all remaining bytes and makes a byte packet
 */
public suspend fun ByteReadChannel.readRemaining(): ByteReadPacket = readRemaining(Long.MAX_VALUE)

public suspend fun ByteReadChannel.readUTF8LineTo(out: Appendable): Boolean {
    return readUTF8LineTo(out, Int.MAX_VALUE)
}

public suspend fun ByteReadChannel.readUTF8Line(): String? {
    return readUTF8Line(Int.MAX_VALUE)
}

public fun ByteReadChannel.cancel(): Boolean = cancel(null)

/**
 * Discards all bytes in the channel and suspends until end of stream.
 */
public suspend fun ByteReadChannel.discard(): Long = discard(Long.MAX_VALUE)

/**
 * Discards exactly [n] bytes or fails if not enough bytes in the channel
 */
public suspend inline fun ByteReadChannel.discardExact(n: Long) {
    if (discard(n) != n) throw EOFException("Unable to discard $n bytes")
}

public suspend fun ByteReadChannel.readAvailable(dst: ByteArray): Int = readAvailable(dst, 0, dst.size)

public suspend fun ByteReadChannel.readFully(dst: ByteArray): Unit = readFully(dst, 0, dst.size)

public expect suspend fun ByteReadChannel.joinTo(dst: ByteWriteChannel, closeOnEnd: Boolean)

/**
 * Reads bytes from receiver channel and writes them to [dst] channel.
 * Closes [dst] channel if fails to read or write with cause exception.
 * @return a number of copied bytes
 */
public suspend fun ByteReadChannel.copyTo(dst: ByteWriteChannel): Long = copyTo(dst, limit = Long.MAX_VALUE)

/**
 * Reads up to [limit] bytes from receiver channel and writes them to [dst] channel.
 * Closes [dst] channel if fails to read or write with cause exception.
 * @return a number of copied bytes
 */
public expect suspend fun ByteReadChannel.copyTo(dst: ByteWriteChannel, limit: Long): Long

/**
 * Reads all the bytes from receiver channel and writes them to [dst] channel and then closes it.
 * Closes [dst] channel if fails to read or write with cause exception.
 * @return a number of copied bytes
 */
public suspend fun ByteReadChannel.copyAndClose(dst: ByteWriteChannel, limit: Long = Long.MAX_VALUE): Long {
    val count = copyTo(dst, limit)
    dst.close()
    return count
}
