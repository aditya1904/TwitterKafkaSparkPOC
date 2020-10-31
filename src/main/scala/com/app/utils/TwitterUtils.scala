package com.app.utils

import com.app.utils.constants.KafkaConstants.{BROKERS, KAFKA_TOPIC}
import com.app.utils.constants.TwitterKeys._
import com.app.kafka.Producer
import org.apache.spark.sql.types.{StringType, StructType}
import twitter4j.conf.{Configuration, ConfigurationBuilder}
import twitter4j.{StallWarning, Status, StatusDeletionNotice, StatusListener}

class TwitterUtils {

  val twitterDevConfiguration: Configuration =
    new ConfigurationBuilder()
      .setOAuthConsumerKey(CONSUMER_KEY)
      .setOAuthConsumerSecret(CONSUMER_SECRET)
      .setOAuthAccessToken(ACCESS_TOKEN)
      .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
      .build

  val producer = new Producer(BROKERS, KAFKA_TOPIC)

  val tweetSchema: StructType = new StructType()
    .add("id", StringType, nullable = true)
    .add("tweet", StringType, nullable = true)
    .add("user", StringType, nullable = true)
    .add("time", StringType, nullable = true)


  private def simpleJSON(status: Status) : String = {
    s"""
       |{
       |"id": "${status.getId}",
       |"user": "${status.getUser.getScreenName}",
       |"tweet": "${status.getText}",
       |"time": "${status.getCreatedAt.getTime}"
       |}""".stripMargin.replaceAll("\n", " ")
  }


  def simpleStatusListener: StatusListener = new StatusListener() {
    override def onStatus(status: Status): Unit = {
      producer.sendTweets(simpleJSON(status))
      println(simpleJSON(status))
    }

    override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}

    override def onTrackLimitationNotice(i: Int): Unit = {}

    override def onScrubGeo(l: Long, l1: Long): Unit = {}

    override def onException(e: Exception): Unit = {
      e.printStackTrace()
    }

    override def onStallWarning(stallWarning: StallWarning): Unit = {
      println(stallWarning.getMessage)
    }
  }
}
