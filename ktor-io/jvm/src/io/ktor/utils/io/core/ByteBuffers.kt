package io.ktor.utils.io.core

import java.nio.*
import kotlin.contracts.*

/**
 * Read at most `dst.remaining()` bytes to the specified [dst] byte buffer and change its position accordingly
 * @return number of bytes copied
 */
public fun ByteReadPacket.readAvailable(dst: ByteBuffer): Int = TODO()

/**
 * Read exactly `dst.remaining()` bytes to the specified [dst] byte buffer and change its position accordingly
 * @return number of bytes copied
 */
public fun ByteReadPacket.readFully(dst: ByteBuffer): Int {
    TODO()
}

/**
 * Write bytes directly to packet builder's segment. Generally shouldn't be used in user's code and useful for
 * efficient integration.
 *
 * Invokes [block] lambda with one byte buffer. [block] lambda should change provided's position accordingly but
 * shouldn't change any other pointers.
 *
 * @param size minimal number of bytes should be available in a buffer provided to the lambda. Should be as small as
 * possible. If [size] is too large then the function may fail because the segments size is not guaranteed to be fixed
 * and not guaranteed that it will be big enough to keep [size] bytes. However it is guaranteed that the segment size
 * is at least 8 bytes long (long integer bytes length)
 */
@OptIn(ExperimentalContracts::class)
public inline fun BytePacketBuilder.writeDirect(size: Int, block: (ByteBuffer) -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    writeByteBufferDirect(size, block)
}

/**
 * Write bytes directly to packet builder's segment. Generally shouldn't be used in user's code and useful for
 * efficient integration.
 *
 * Invokes [block] lambda with one byte buffer. [block] lambda should change provided's position accordingly but
 * shouldn't change any other pointers.
 *
 * @param size minimal number of bytes should be available in a buffer provided to the lambda. Should be as small as
 * possible. If [size] is too large then the function may fail because the segments size is not guaranteed to be fixed
 * and not guaranteed that it will be big enough to keep [size] bytes. However it is guaranteed that the segment size
 * is at least 8 bytes long (long integer bytes length)
 */
@OptIn(ExperimentalContracts::class)
public inline fun BytePacketBuilder.writeByteBufferDirect(size: Int, block: (ByteBuffer) -> Unit): Int {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    TODO()
}

@OptIn(ExperimentalContracts::class)
public inline fun ByteReadPacket.readDirect(size: Int, block: (ByteBuffer) -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    TODO()
}
