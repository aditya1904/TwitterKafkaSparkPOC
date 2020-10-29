package com.app.kafka

import com.app.utils.SparkUtils
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils}
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent


class Consumer(bootstrapServers: String, topic: String) {

  val props: Map[String, Object] = Map[String, Object] (
    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> bootstrapServers,
    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest"
  )

  val spark = new SparkUtils

  def receiveTweets() : Unit = {
    val stream = KafkaUtils.createDirectStream[String, String](
      spark.streamingContext,
      PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array(topic), props)
    )
  }

}
