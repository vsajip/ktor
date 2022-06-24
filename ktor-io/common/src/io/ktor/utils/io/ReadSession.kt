package io.ktor.utils.io

import io.ktor.utils.io.bits.Memory
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*

/**
 * Await until at least [desiredSize] is available for read or EOF and invoke [block] function. The block function
 * should never capture a provided [Memory] instance outside otherwise an undefined behaviour may occur including
 * accidental crash or data corruption. Block function should return number of bytes consumed or 0.
 *
 * Specifying [desiredSize] larger than the channel's capacity leads to block function invocation earlier
 * when the channel is full. So specifying too big [desiredSize] is identical to specifying [desiredSize] equal to
 * the channel's capacity. The other case when a provided memory range could be less than [desiredSize] is that
 * all the requested bytes couldn't be represented as a single memory range due to internal implementation reasons.
 *
 * @return number of bytes consumed, possibly 0
 */
public suspend inline fun ByteReadChannel.read(
    desiredSize: Int = 1,
    block: (source: Memory, start: Long, endExclusive: Long) -> Int
): Int {
    val buffer = requestBuffer(desiredSize)

    try {
        val bytesRead = block(buffer.memory, buffer.readPosition.toLong(), buffer.writePosition.toLong())
        completeReadingFromBuffer(buffer, bytesRead)
        return bytesRead
    } catch (cause: Throwable) {
        completeReadingFromBuffer(buffer, 0)
        throw cause
    }

    // we don't use finally here because of KT-37279
}

@PublishedApi
internal suspend fun ByteReadChannel.completeReadingFromBuffer(buffer: Buffer?, bytesRead: Int) {
    check(bytesRead >= 0) { "bytesRead shouldn't be negative: $bytesRead" }

    if (buffer is ChunkBuffer && buffer !== ChunkBuffer.Empty) {
        buffer.release(ChunkBuffer.Pool)
        discard(bytesRead.toLong())
    }
}

@PublishedApi
internal suspend fun ByteReadChannel.requestBuffer(desiredSize: Int): ChunkBuffer {
    val chunk = ChunkBuffer.Pool.borrow()
    val copied = peekTo(
        chunk.memory, chunk.writePosition.toLong(), 0L, desiredSize.toLong(), chunk.writeRemaining.toLong()
    )

    chunk.commitWritten(copied.toInt())

    return chunk
}
