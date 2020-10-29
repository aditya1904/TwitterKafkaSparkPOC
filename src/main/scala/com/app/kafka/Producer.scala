package com.app.kafka

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class Producer(bootstrapServers: String, topic: String) {

  val props = new Properties()

  props.put("bootstrap.servers", bootstrapServers)
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("topic", topic)

  private lazy val producer = new KafkaProducer[String, String](props)

  def sendTweets (tweet: String): Unit = {
    producer.send(new ProducerRecord[String, String](props.getProperty("topic"), tweet))
    producer.flush()
  }

}
