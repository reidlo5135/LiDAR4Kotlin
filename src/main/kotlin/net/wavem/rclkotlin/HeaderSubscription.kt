package net.wavem.rclkotlin

import id.jros2client.impl.rmw.DdsNameMapper
import id.jros2client.impl.rmw.RmwConstants
import id.jrosclient.utils.RosNameUtils
import net.wavem.rclkotlin.std_msgs.Header
import pinorobotics.rtpstalk.RtpsTalkClient
import pinorobotics.rtpstalk.RtpsTalkConfiguration
import pinorobotics.rtpstalk.messages.RtpsTalkDataMessage
import rx.Observable
import rx.subjects.PublishSubject
import java.util.concurrent.Flow


class HeaderSubscription {
    private val ddsClient : RtpsTalkClient = RtpsTalkClient(
        RtpsTalkConfiguration.Builder()
            .networkInterface("lo")
            .build()
    )

    private val rclMapper : DdsNameMapper = DdsNameMapper(RosNameUtils())
    private val dataObservable: PublishSubject<String?> = PublishSubject.create()
    private val rclHeaderTopic : String = "/header"
    val rclMessageService : RCLMessageService = RCLMessageService()
    fun getDataObservable() : Observable<String?> {
        return dataObservable
    }

    fun create() {
        ddsClient.subscribe("rt/header", "std_msgs::msg::dds_::Header_", RmwConstants.DEFAULT_SUBSCRIBER_QOS, object :
            Flow.Subscriber<RtpsTalkDataMessage> {
            private lateinit var subscription : Flow.Subscription

            override fun onSubscribe(subscription : Flow.Subscription) {
                this.subscription = subscription
                println("Subscription registerd")
                subscription.request(1)
            }

            override fun onNext(message : RtpsTalkDataMessage) {
                message.data().ifPresent { data -> println("Received " + Header.read(data)) }
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
    val headerSubscription : HeaderSubscription = HeaderSubscription()
    headerSubscription.create()

    headerSubscription.getDataObservable().subscribe() {newValue ->
        if (newValue != null) {
            println("header new value : $newValue")
        }
    }
}