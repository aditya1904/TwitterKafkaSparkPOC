package com.app.utils

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

class SparkUtils {

  val appname = "Twitter Kafka Integration"
  val interval = 5

  val sparkConf: SparkConf = new SparkConf()
                      .setMaster("local[*]")
                      .setAppName(appname)

  val streamingContext : StreamingContext = new StreamingContext(sparkConf, Seconds(interval))

}
