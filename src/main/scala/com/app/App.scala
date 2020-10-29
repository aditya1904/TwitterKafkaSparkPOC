package com.app

import com.app.utils.{SparkUtils, TwitterUtils}
import twitter4j.{FilterQuery, TwitterStreamFactory}
object App {
  def main(args: Array[String]): Unit = {

    val spark = new SparkUtils
    val twitter = new TwitterUtils
    val sc = spark.streamingContext.sparkContext
    sc.setLogLevel("ERROR")

    val tweetstream = new TwitterStreamFactory(twitter.twitterDevConfiguration).getInstance()

    tweetstream.addListener(twitter.simpleStatusListener)

    tweetstream.filter(new FilterQuery().track(Array("#CSKvKKR")))

  }
}
