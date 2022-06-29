package io.ktor.utils.io

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import io.ktor.utils.io.internal.*
import java.nio.*

/**
 * Channel for asynchronous reading of sequences of bytes.
 * This is a **single-reader channel**.
 *
 * Operations on this channel cannot be invoked concurrently.
 */
public actual interface ByteReadChannel {
    /**
     * Returns number of bytes that can be read without suspension. Read operations do no suspend and return
     * immediately when this number is at least the number of bytes requested for read.
     */
    public actual val availableForRead: Int

    /**
     * Returns `true` if the channel is closed and no remaining bytes are available for read.
     * It implies that [availableForRead] is zero.
     */
    public actual val isClosedForRead: Boolean

    public actual val isClosedForWrite: Boolean

    /**
     * A closure causes exception or `null` if closed successfully or not yet closed
     */
    public actual val closedCause: Throwable?

    /**
     * Number of bytes read from the channel.
     * It is not guaranteed to be atomic so could be updated in the middle of long-running read operation.
     */
    public actual val totalBytesRead: Long

    /**
     * Invokes [block] if it is possible to read at least [min] byte
     * providing byte buffer to it so lambda can read from the buffer
     * up to [ByteBuffer.available] bytes. If there are no [min] bytes available then the invocation returns 0.
     *
     * Warning: it is not guaranteed that all of available bytes will be represented as a single byte buffer
     * eg: it could be 4 bytes available for read but the provided byte buffer could have only 2 available bytes:
     * in this case you have to invoke read again (with decreased [min] accordingly).
     *
     * @param min amount of bytes available for read, should be positive
     * @param block to be invoked when at least [min] bytes available
     *
     * @return number of consumed bytes or -1 if the block wasn't executed.
     */
    public fun readAvailable(min: Int = 1, block: (ByteBuffer) -> Unit): Int

    /**
     * Reads all available bytes to [dst] buffer and returns immediately or suspends if no bytes available
     * @return number of bytes were read or `-1` if the channel has been closed
     */
    public actual suspend fun readAvailable(dst: ByteArray, offset: Int, length: Int): Int
    public suspend fun readAvailable(dst: ByteBuffer): Int

    /**
     * Reads all [length] bytes to [dst] buffer or fails if channel has been closed.
     * Suspends if not enough bytes available.
     */
    public actual suspend fun readFully(dst: ByteArray, offset: Int, length: Int)
    public suspend fun readFully(dst: ByteBuffer): Int

    /**
     * Reads the specified amount of bytes and makes a byte packet from them. Fails if channel has been closed
     * and not enough bytes available.
     */
    public actual suspend fun readPacket(size: Int): ByteReadPacket

    /**
     * Reads up to [limit] bytes and makes a byte packet or until end of stream encountered.
     */
    public actual suspend fun readRemaining(limit: Long): ByteReadPacket

    /**
     * Reads a long number (suspending if not enough bytes available) or fails if channel has been closed
     * and not enough bytes.
     */
    public actual suspend fun readLong(): Long

    /**
     * Reads an int number (suspending if not enough bytes available) or fails if channel has been closed
     * and not enough bytes.
     */
    public actual suspend fun readInt(): Int

    /**
     * Reads a short number (suspending if not enough bytes available) or fails if channel has been closed
     * and not enough bytes.
     */
    public actual suspend fun readShort(): Short

    /**
     * Reads a byte (suspending if no bytes available yet) or fails if channel has been closed
     * and not enough bytes.
     */
    public actual suspend fun readByte(): Byte

    /**
     * Reads a boolean value (suspending if no bytes available yet) or fails if channel has been closed
     * and not enough bytes.
     */
    public actual suspend fun readBoolean(): Boolean

    /**
     * Reads double number (suspending if not enough bytes available) or fails if channel has been closed
     * and not enough bytes.
     */
    public actual suspend fun readDouble(): Double

    /**
     * Reads float number (suspending if not enough bytes available) or fails if channel has been closed
     * and not enough bytes.
     */
    public actual suspend fun readFloat(): Float

    /**
     * Reads a line of UTF-8 characters to the specified [out] buffer up to [limit] characters.
     * Supports both CR-LF and LF line endings.
     * Throws an exception if the specified [limit] has been exceeded.
     *
     * @return `true` if line has been read (possibly empty) or `false` if channel has been closed
     * and no characters were read.
     */
    public actual suspend fun <A : Appendable> readUTF8LineTo(out: A, limit: Int): Boolean

    /**
     * Reads a line of UTF-8 characters up to [limit] characters.
     * Supports both CR-LF and LF line endings.
     * Throws an exception if the specified [limit] has been exceeded.
     *
     * @return a line string with no line endings or `null` of channel has been closed
     * and no characters were read.
     */
    public actual suspend fun readUTF8Line(limit: Int): String?

    /**
     * Invokes [consumer] when it will be possible to read at least [min] bytes
     * providing byte buffer to it so lambda can read from the buffer
     * up to [ByteBuffer.remaining] bytes. If there are no [min] bytes available then the invocation could
     * suspend until the requirement will be met.
     *
     * If [min] is zero then the invocation will suspend until at least one byte available.
     *
     * Warning: it is not guaranteed that all of remaining bytes will be represented as a single byte buffer
     * eg: it could be 4 bytes available for read but the provided byte buffer could have only 2 remaining bytes:
     * in this case you have to invoke read again (with decreased [min] accordingly).
     *
     * It will fail with [EOFException] if not enough bytes ([availableForRead] < [min]) available
     * in the channel after it is closed.
     *
     * [consumer] lambda should modify buffer's position accordingly. It also could temporarily modify limit however
     * it should restore it before return. It is not recommended to access any bytes of the buffer outside of the
     * provided byte range [position(); limit()) as there could be any garbage or incomplete data.
     *
     * @param min amount of bytes available for read, should be positive or zero
     * @param consumer to be invoked when at least [min] bytes available for read
     */
    public suspend fun read(min: Int = 1, consumer: (ByteBuffer) -> Unit)

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
    public actual fun cancel(cause: Throwable?): Boolean

    /**
     * Discard up to [max] bytes
     *
     * @return number of bytes were discarded
     */
    public actual suspend fun discard(max: Long): Long

    /**
     * Suspend until the channel has bytes to read or gets closed. Throws exception if the channel was closed with an error.
     */
    public actual suspend fun awaitContent()

    public actual companion object {
        public actual val Empty: ByteReadChannel by lazy { ByteChannel().apply { close() } }
    }
}

public actual suspend fun ByteReadChannel.joinTo(dst: ByteWriteChannel, closeOnEnd: Boolean) {
    require(dst !== this)

    return joinToImplSuspend(dst, closeOnEnd)
}

private suspend fun ByteReadChannel.joinToImplSuspend(dst: ByteWriteChannel, close: Boolean) {
    copyTo(dst, Long.MAX_VALUE)
    if (close) {
        dst.close()
    } else {
        dst.flush()
    }
}

/**
 * Reads up to [limit] bytes from receiver channel and writes them to [dst] channel.
 * Closes [dst] channel if fails to read or write with cause exception.
 * @return a number of copied bytes
 */
public actual suspend fun ByteReadChannel.copyTo(dst: ByteWriteChannel, limit: Long): Long {
    require(this !== dst)

    if (limit == 0L) {
        return 0L
    }

    TODO()
}

/**
 * TODO
 * Reads all the bytes from receiver channel and builds a packet that is returned unless the specified [limit] exceeded.
 * It will simply stop reading and return packet of size [limit] in this case
 */
/*suspend fun ByteReadChannel.readRemaining(limit: Int = Int.MAX_VALUE): ByteReadPacket {
    val buffer = JavaNioAccess.BufferPool.borrow()
    val packet = WritePacket()

    try {
        var copied = 0L

        while (copied < limit) {
            buffer.clear()
            if (limit - copied < buffer.limit()) {
                buffer.limit((limit - copied).toInt())
            }
            val size = readAvailable(buffer)
            if (size == -1) break

            buffer.flip()
            packet.writeFully(buffer)
            copied += size
        }

        return packet.build()
    } catch (t: Throwable) {
        packet.release()
        throw t
    } finally {
        JavaNioAccess.BufferPool.recycle(buffer)
    }
}*/
