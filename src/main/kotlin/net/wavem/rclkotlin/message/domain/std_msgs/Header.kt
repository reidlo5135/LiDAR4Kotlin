package net.wavem.rclkotlin.message.domain.std_msgs

import net.wavem.rclkotlin.message.domain.builtin_interfaces.Time
import java.nio.ByteBuffer
import java.nio.ByteOrder


@JvmRecord
data class Header(
    val stamp : Time,
    val frame_id : String
) {

    companion object {
        fun read(data : ByteArray) : Header {
            val buf : ByteBuffer = ByteBuffer.wrap(data)
            buf.order(ByteOrder.LITTLE_ENDIAN)

            val time : Time = Time.read(data)
            buf.position(8)

            var len : Int = buf.getInt()
            var frame_id = ""

            while (len-- > 0) frame_id += Char(buf.get().toUShort())
            return Header(time, frame_id)
        }
    }
}