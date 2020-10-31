package com.app.utils.constants

import org.apache.spark.sql.types.{StringType, StructType}

object Constants {

  val OUTPUT_PATH = "/mnt/GYAN_VIGYAN/IdeaProjects/TwitterKafka/data/"

  val tweetSchema: StructType = new StructType()
    .add("id", StringType, nullable = true)
    .add("tweet", StringType, nullable = true)
    .add("user", StringType, nullable = true)
    .add("time", StringType, nullable = true)
}
