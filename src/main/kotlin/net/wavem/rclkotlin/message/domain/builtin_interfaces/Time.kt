package net.wavem.rclkotlin.message.domain.builtin_interfaces

import java.nio.ByteBuffer
import java.nio.ByteOrder

@JvmRecord
data class Time(
    val sec : Int,
    val nanosec : Int
){
    companion object {
        fun read(data : ByteArray) : Time {
            val buf : ByteBuffer = ByteBuffer.wrap(data)
            buf.order(ByteOrder.LITTLE_ENDIAN)
            val sec : Int = buf.getInt()
            val nanosec : Int = buf.getInt()
            return Time(sec, nanosec)
        }
    }
}