package com.app.utils

import com.app.kafka.Producer
import com.app.utils.constants.TwitterKeys._
import twitter4j.conf.{Configuration, ConfigurationBuilder}
import twitter4j.{StallWarning, Status, StatusDeletionNotice, StatusListener}

class TwitterUtils (kafkaproducer: Producer) {

  val twitterDevConfiguration: Configuration =
    new ConfigurationBuilder()
      .setOAuthConsumerKey(CONSUMER_KEY)
      .setOAuthConsumerSecret(CONSUMER_SECRET)
      .setOAuthAccessToken(ACCESS_TOKEN)
      .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
      .setTweetModeExtended(true)
      .build

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
      kafkaproducer.sendTweets(simpleJSON(status))
//      println(simpleJSON(status))
    }

    override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}

    override def onTrackLimitationNotice(i: Int): Unit = {}

    override def onScrubGeo(l: Long, l1: Long): Unit = {}

    override def onException(e: Exception): Unit = {e.printStackTrace()}

    override def onStallWarning(stallWarning: StallWarning): Unit = {println(stallWarning.getMessage)}
  }
}
