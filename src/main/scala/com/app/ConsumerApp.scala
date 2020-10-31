package com.app

import com.app.kafka.Consumer
import com.app.utils.constants.Constants.{OUTPUT_PATH, tweetSchema}
import com.app.utils.{MyDateFormat, SparkUtils, Tweet}
import org.apache.spark.sql.{Row, SparkSession}
import org.json4s._
import org.json4s.native.JsonMethods._

object ConsumerApp {
  def main(args: Array[String]): Unit = {

    implicit val formats: Formats = new Formats {
      override val dateFormat = new MyDateFormat
    }

    if (args.length != 2) {
      println(
        "Argument Required: <Kafka-topic> <Kafka-Brokers-Comma-Separated>")
      System.exit(1);
    }

    val sc = SparkUtils.streamingContext.sparkContext
    sc.setLogLevel("ERROR")

    val consumer = new Consumer(args(1), args(0))

    val consumedTweets = consumer.receiveTweets()

    val tweetObjects = consumedTweets.map(x => parse(x.value()).extract[Tweet])

    tweetObjects.foreachRDD(rdd => {
      SparkSession.builder
        .config(rdd.sparkContext.getConf)
        .getOrCreate
        .createDataFrame(rdd.map(
                           t => Row(t.id, t.tweet, t.user, t.time)
                          ), tweetSchema)
                    .write
                    .mode("Append")
                    .csv(OUTPUT_PATH)
    })

    SparkUtils.streamingContext.start()
    SparkUtils.streamingContext.awaitTermination()

  }
}
