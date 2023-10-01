package net.wavem.rclkotlin

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import pinorobotics.rtpstalk.RtpsTalkClient;
import pinorobotics.rtpstalk.RtpsTalkConfiguration;
import id.jros2client.impl.rmw.RmwConstants
import net.wavem.rclkotlin.message.domain.sensor_msgs.LaserScan
import pinorobotics.rtpstalk.messages.RtpsTalkDataMessage
import rx.Observable
import rx.subjects.PublishSubject

class LiDARScanSubscription {
    private val ddsClient : RtpsTalkClient = RtpsTalkClient(
        RtpsTalkConfiguration.Builder()
            .networkInterface(DDS_NETWORK_INTF_TYPE)
            .build()
    )

    private val dataObservable: PublishSubject<LaserScan?> = PublishSubject.create()
    fun getDataObservable() : Observable<LaserScan?> {
        return dataObservable
    }

    fun create() {
        ddsClient.subscribe(DDS_TOPIC, DDS_MESSAGE_TYPE, RmwConstants.DEFAULT_SUBSCRIBER_QOS, object : Subscriber<RtpsTalkDataMessage> {
            private lateinit var subscription : Subscription

            override fun onSubscribe(subscription : Subscription) {
                this.subscription = subscription
                println("Subscription registered")
                subscription.request(1)
            }

            override fun onNext(message : RtpsTalkDataMessage) {
                message.data().ifPresent { data ->
                    dataObservable.onNext(LaserScan.read(data))
                }
                subscription.request(1)
            }

            override fun onError(throwable : Throwable) {
                throwable.printStackTrace()
            }

            override fun onComplete() {
                subscription.cancel()
            }
        })
    }

    companion object {
        const val DDS_NETWORK_INTF_TYPE : String = "lo"
        const val DDS_TOPIC : String = "rt/scan"
        const val DDS_MESSAGE_TYPE : String = "sensor_msgs::msg::dds_::LaserScan_"
    }
}

fun main(args : Array<String>) {
    val liDARScanSubscription : LiDARScanSubscription = LiDARScanSubscription()
    liDARScanSubscription.create()

    liDARScanSubscription.getDataObservable().subscribe() {newValue ->
        if (newValue != null) {
            println("LiDAR Scan Data updated : $newValue")
        }
    }
}