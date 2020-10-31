package com.app

import com.app.utils.{MyDateFormat, SparkUtils, TwitterUtils}
import org.json4s._
import twitter4j.{FilterQuery, TwitterStreamFactory}

object ProducerApp {
  def main(args: Array[String]): Unit = {

    implicit val formats: Formats = new Formats {

      override val dateFormat = new MyDateFormat
    }

    if (args.length != 1){
      println("Argument Required: <Twitter-topic-to-track>")
      System.exit(1);
    }

    val twitterTopic = Array(args(0))
    val twitter = new TwitterUtils
    val sc = SparkUtils.streamingContext.sparkContext
    sc.setLogLevel("ERROR")

    val tweetstream = new TwitterStreamFactory(twitter.twitterDevConfiguration).getInstance()
    tweetstream.addListener(twitter.simpleStatusListener)
    tweetstream.filter(new FilterQuery().track(twitterTopic))

  }
}
