@file:Suppress("RedundantModalityModifier")

package io.ktor.utils.io.core

import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.internal.*
import io.ktor.utils.io.pool.*

/**
 * A builder that provides ability to build byte packets with no knowledge of it's size.
 * Unlike Java's ByteArrayOutputStream it doesn't copy the whole content every time it's internal buffer overflows
 * but chunks buffers instead. Packet building via [build] function is O(1) operation and only does instantiate
 * a new [ByteReadPacket]. Once a byte packet has been built via [build] function call, the builder could be
 * reused again. You also can discard all written bytes via [reset] or [release]. Please note that an instance of
 * builder need to be terminated either via [build] function invocation or via [release] call otherwise it will
 * cause byte buffer leak so that may have performance impact.
 *
 * Byte packet builder is also an [Appendable] so it does append UTF-8 characters to a packet
 *
 * ```
 * buildPacket {
 *     listOf(1,2,3).joinTo(this, separator = ",")
 * }
 * ```
 */
public class BytePacketBuilder {

    /**
     * Number of bytes written to the builder after the creation or the last reset.
     */
    public val size: Int
        get() = TODO()

    /**
     * If no bytes were written or the builder has been reset.
     */
    public val isEmpty: Boolean
        get() = TODO()

    /**
     * If at least one byte was written after the creation or the last reset.
     */
    public val isNotEmpty: Boolean
        get() = TODO()

    public fun append(value: Char): BytePacketBuilder {
        TODO()
    }

    public fun append(value: CharSequence): BytePacketBuilder {
        TODO()
    }

    public fun append(value: CharSequence, startIndex: Int, endIndex: Int): BytePacketBuilder {
        TODO()
    }

    /**
     * Builds byte packet instance and resets builder's state to be able to build another one packet if needed
     */
    public fun build(): ByteReadPacket {
        TODO()
    }

    override fun toString(): String {
        return "BytePacketBuilder($size bytes written)"
    }
}
