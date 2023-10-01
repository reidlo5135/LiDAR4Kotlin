package net.wavem.rclkotlin.domain.sensor_msgs

import net.wavem.rclkotlin.domain.std_msgs.Header
import java.nio.ByteBuffer
import java.nio.ByteOrder

@JvmRecord
data class LaserScan(
    val header : Header,
    val angle_min : Float,
    val angle_max : Float,
    val angle_increment : Float,
    val time_increment : Float,
    val scan_time : Float,
    val range_min : Float,
    val range_max : Float,
    val ranges : FloatArray,
    val intensities:  FloatArray
) {
    companion object {
        fun read(data : ByteArray) : LaserScan {
            val buf : ByteBuffer = ByteBuffer.wrap(data)
            buf.order(ByteOrder.LITTLE_ENDIAN)

            val header : Header = Header.read(data)
            val headerSize : Int = 14 + header.frame_id.length
            buf.position(headerSize)

            val angle_min : Float = buf.getFloat()
            val angle_max : Float = buf.getFloat()
            val angle_increment : Float = buf.getFloat()
            val time_increment : Float = buf.getFloat()
            val scan_time : Float = buf.getFloat()
            val range_min : Float = buf.getFloat()
            val range_max : Float = buf.getFloat()

            val rangesSize : Int = 2048
            val intensitiesSize : Int = 1024

            val ranges : FloatArray = FloatArray(rangesSize)
            val intensities : FloatArray = FloatArray(intensitiesSize)

            for (i in 0..<rangesSize) {
                ranges[i] = buf.getFloat()
            }

            for (i in 0..<intensitiesSize) {
                intensities[i] = buf.getFloat()
            }

            return LaserScan(
                header = header,
                angle_min = angle_min,
                angle_max = angle_max,
                angle_increment = angle_increment,
                time_increment = time_increment,
                scan_time = scan_time,
                range_min = range_min,
                range_max = range_max,
                ranges = ranges,
                intensities = intensities
            )
        }
    }

    override fun equals(other : Any?) : Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LaserScan

        if (!ranges.contentEquals(other.ranges)) return false
        if (!intensities.contentEquals(other.intensities)) return false

        return true
    }

    override fun hashCode() : Int {
        var result : Int = ranges.contentHashCode()
        result = 31 * result + intensities.contentHashCode()

        return result
    }
}
