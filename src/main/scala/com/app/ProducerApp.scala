package com.app

import com.app.kafka.Producer
import com.app.utils.{SparkUtils, TwitterUtils}
import twitter4j.{FilterQuery, TwitterStreamFactory}

object ProducerApp {
  def main(args: Array[String]): Unit = {

    if (args.length != 3){
      println("Argument Required: <Twitter-topic-to-track> <Kafka-topic> <Kafka-Brokers-Comma-Separated>")
      System.exit(1);
    }

    val (twitterTopic, kafkatopic, brokers) = (args(0), args(1), args(2))

    val kafkaProducer = new Producer(bootstrapServers = brokers, topic= kafkatopic)

    val twitter = new TwitterUtils(kafkaProducer)

    val sc = SparkUtils.streamingContext.sparkContext
    sc.setLogLevel("ERROR")

    val tweetstream = new TwitterStreamFactory(twitter.twitterDevConfiguration).getInstance()
    tweetstream.addListener(twitter.simpleStatusListener)
    tweetstream.filter(new FilterQuery().track(twitterTopic)
              .language("en"))

  }
}
