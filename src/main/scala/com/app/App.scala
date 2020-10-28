package com.app

import com.app.utils.{SparkUtils, TwitterUtils}
import org.apache.spark.streaming.twitter.TwitterUtils
object App {
  def main(args: Array[String]): Unit = {

    val spark = new SparkUtils
    val twitter = new TwitterUtils
    val sc = spark.streamingContext.sparkContext
    sc.setLogLevel("ERROR")
    val tweets = TwitterUtils.createStream(spark.streamingContext,
                                                    Some(twitter.twitterAuth))

    val ipltweets = tweets.filter(_.getLang == "en")

    ipltweets.saveAsTextFiles("iplt20", "json")

    spark.streamingContext.start()
    spark.streamingContext.awaitTermination()

  }
}
