package io.ktor.utils.io.core

import kotlin.contracts.*

public expect val PACKET_MAX_COPY_SIZE: Int

/**
 * Build a byte packet in [block] lambda. Creates a temporary builder and releases it in case of failure
 */
@OptIn(ExperimentalContracts::class)
public inline fun buildPacket(block: BytePacketBuilder.() -> Unit): ByteReadPacket {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    TODO()
}
