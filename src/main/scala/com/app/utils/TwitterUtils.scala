package com.app.utils

import com.app.constants.TwitterKeys._
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder

class TwitterUtils {

  val twitterDevConfiguration: ConfigurationBuilder =
    new ConfigurationBuilder()
        .setDebugEnabled(true)
        .setOAuthConsumerKey(CONSUMER_KEY)
        .setOAuthConsumerSecret(CONSUMER_SECRET)
        .setOAuthAccessToken(ACCESS_TOKEN)
        .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)

  val twitterAuth = new OAuthAuthorization(twitterDevConfiguration.build())

}
