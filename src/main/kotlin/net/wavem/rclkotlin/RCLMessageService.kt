package net.wavem.rclkotlin

import java.nio.ByteBuffer
import java.nio.ByteOrder

class RCLMessageService {

    fun toByteArray(data : String) : ByteArray {
        val buf : ByteBuffer = ByteBuffer.allocate(Integer.BYTES * 2 + data.length + 1)
        buf.order(ByteOrder.LITTLE_ENDIAN)
        buf.putInt(data.length + 1)
        buf.put(data.toByteArray())
        return buf.array()
    }

    fun read(data : ByteArray) : String {
        val buf = ByteBuffer.wrap(data)
        buf.order(ByteOrder.LITTLE_ENDIAN)
        buf.getInt()
        val messageBytes = ByteArray(data.size - Integer.BYTES)
        buf.get(messageBytes)
        return String(messageBytes)
    }
}