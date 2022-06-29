@file:Suppress("RedundantModalityModifier", "FunctionName")

package io.ktor.utils.io.core

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.internal.*
import io.ktor.utils.io.pool.*

/**
 * Read-only immutable byte packet. Could be consumed only once however it does support [copy] that doesn't copy every byte
 * but creates a new view instead. Once packet created it should be either completely read (consumed) or released
 * via [release].
 */
public class ByteReadPacket {
    /**
     * Returns a copy of the packet. The original packet and the copy could be used concurrently. Both need to be
     * either completely consumed or released via [release]
     */
    public final fun copy(): ByteReadPacket = TODO()

    override fun toString(): String = TODO()

    public companion object {
        public val Empty: ByteReadPacket = TODO()
    }
}

public expect fun ByteReadPacket(
    array: ByteArray,
    offset: Int = 0,
    length: Int = array.size,
    block: (ByteArray) -> Unit
): ByteReadPacket

@Suppress("NOTHING_TO_INLINE")
public inline fun ByteReadPacket(array: ByteArray, offset: Int = 0, length: Int = array.size): ByteReadPacket {
    return ByteReadPacket(array, offset, length) {}
}
