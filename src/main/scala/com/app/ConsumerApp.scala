package com.app

import com.app.utils.constants.KafkaConstants.{BROKERS, KAFKA_TOPIC}
import com.app.kafka.Consumer
import com.app.utils.{MyDateFormat, SparkUtils, Tweet, TwitterUtils}
import org.apache.spark.sql.{Row, SparkSession}
import org.json4s._
import org.json4s.native.JsonMethods._

object ConsumerApp {
  def main(args: Array[String]): Unit = {

    implicit val formats: Formats = new Formats {

      override val dateFormat = new MyDateFormat
    }
    val sc = SparkUtils.streamingContext.sparkContext
    sc.setLogLevel("ERROR")

    val tweetSchema = new TwitterUtils().tweetSchema

    val consumer = new Consumer(BROKERS, KAFKA_TOPIC)

    val consumedTweets = consumer.receiveTweets()

    val tweetObjects = consumedTweets.map(x => parse(x.value()).extract[Tweet])

    tweetObjects.foreachRDD(rdd => {
      SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate.createDataFrame(rdd.map(t => Row(
        t.id,
        t.tweet,
        t.user,
        t.time
      )), tweetSchema).show()
    })

    SparkUtils.streamingContext.start()

    SparkUtils.streamingContext.awaitTermination()

  }
}
