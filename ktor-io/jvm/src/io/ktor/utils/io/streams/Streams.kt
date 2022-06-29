package io.ktor.utils.io.streams

import io.ktor.utils.io.core.*
import java.io.*

/**
 * Write the whole packet to the stream once it is built via [builder] lambda
 */
public fun OutputStream.writePacket(builder: BytePacketBuilder.() -> Unit) {
    writePacket(buildPacket(block = builder))
}

/**
 * Write the whole [packet] to the stream
 */
public fun OutputStream.writePacket(packet: ByteReadPacket) {
    TODO()
}

/**
 * Read a packet of exactly [n] bytes
 */
public fun InputStream.readPacketExact(n: Long): ByteReadPacket = TODO()

/**
 * Read a packet of at least [n] bytes or all remaining. Does fail if not enough bytes remaining.
 */
public fun InputStream.readPacketAtLeast(n: Long): ByteReadPacket = TODO()

/**
 * Read a packet of at most [n] bytes. Resulting packet could be empty however this function always reads
 * as much bytes as possible.
 */
public fun InputStream.readPacketAtMost(n: Long): ByteReadPacket = TODO()

/**
 * Creates [InputStream] adapter to the packet
 */
public fun ByteReadPacket.inputStream(): InputStream {
    TODO()
}

/**
 * Creates [Reader] from the byte packet that decodes UTF-8 characters
 */
public fun ByteReadPacket.readerUTF8(): Reader {
    TODO()
}

/**
 * Creates [OutputStream] adapter to the builder
 */
public fun BytePacketBuilder.outputStream(): OutputStream {
    TODO()
}

/**
 * Creates [Writer] that encodes all characters in UTF-8 encoding
 */
public fun BytePacketBuilder.writerUTF8(): Writer {
    TODO()
}
