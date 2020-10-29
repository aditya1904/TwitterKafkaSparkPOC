package com.app.utils

import com.app.constants.TwitterKeys._
import twitter4j.{StallWarning, Status, StatusDeletionNotice, StatusListener}
import twitter4j.conf.{Configuration, ConfigurationBuilder}

class TwitterUtils {

  val twitterDevConfiguration: Configuration =
    new ConfigurationBuilder()
      .setOAuthConsumerKey(CONSUMER_KEY)
      .setOAuthConsumerSecret(CONSUMER_SECRET)
      .setOAuthAccessToken(ACCESS_TOKEN)
      .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
      .build

  def simpleStatusListener: StatusListener = new StatusListener() {
    override def onStatus(status: Status): Unit = {
      println("################## * NEW TWEET * ######################")
      println(status.getText)
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
