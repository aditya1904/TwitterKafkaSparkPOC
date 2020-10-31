package com.app.utils
import java.util.{Date, TimeZone}

import org.json4s.DateFormat
import org.json4s.ParserUtil.ParseException

class MyDateFormat extends Serializable with DateFormat {

  def parse(s: String): Option[Date] = try {
    Some(formatter.parse(s))
  } catch {
    case e: ParseException => throw(e)
  }

  def format(d: Date): String = formatter.format(d)

  def timezone: TimeZone = formatter.getTimeZone

  private[this] def formatter = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
}