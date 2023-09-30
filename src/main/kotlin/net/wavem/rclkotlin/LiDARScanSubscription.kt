package net.wavem.rclkotlin

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import pinorobotics.rtpstalk.RtpsTalkClient;
import pinorobotics.rtpstalk.RtpsTalkConfiguration;
import id.jros2client.impl.rmw.DdsNameMapper;
import id.jros2client.impl.rmw.RmwConstants
import id.jrosclient.utils.RosNameUtils
import net.wavem.rclkotlin.sensor_msgs.LaserScan
import pinorobotics.rtpstalk.messages.RtpsTalkDataMessage
import rx.Observable
import rx.subjects.PublishSubject
import java.nio.ByteBuffer
import java.nio.ByteOrder

class LiDARScanSubscription {
    private val ddsClient : RtpsTalkClient = RtpsTalkClient(
        RtpsTalkConfiguration.Builder()
            .networkInterface("lo")
            .build()
    )

    private val dataObservable: PublishSubject<LaserScan?> = PublishSubject.create()
    fun getDataObservable() : Observable<LaserScan?> {
        return dataObservable
    }

    fun create() {
        ddsClient.subscribe("rt/scan", "sensor_msgs::msg::dds_::LaserScan_", RmwConstants.DEFAULT_SUBSCRIBER_QOS, object : Subscriber<RtpsTalkDataMessage> {
            private lateinit var subscription : Subscription

            override fun onSubscribe(subscription : Subscription) {
                this.subscription = subscription
                println("Subscription registerd")
                subscription.request(1)
            }

            override fun onNext(message : RtpsTalkDataMessage) {
                message.data().ifPresent {
                    data -> println("Received " + LaserScan.read(data))
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