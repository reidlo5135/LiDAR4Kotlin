package net.wavem.rclkotlin.std_msgs

import id.jrosmessages.Message
import id.jrosmessages.MessageMetadata
import id.jrosmessages.RosInterfaceType
import id.xfunction.XJson
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

@JvmRecord
internal data class StdString(
    val data : String
) {

    companion object {
        fun read(data : ByteArray) : StdString {
            val buf : ByteBuffer = ByteBuffer.wrap(data)
            buf.order(ByteOrder.LITTLE_ENDIAN)
            var len : Int = buf.getInt()
            var data : String = ""
            while (len-- > 0) data += buf.get().toChar()
            return StdString(data)
        }
    }
}