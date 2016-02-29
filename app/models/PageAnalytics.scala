package models

import java.time.{LocalDate}

import play.api.libs.json.Json

object PageAnalytics {
  implicit val pageAnalyticsFormat = Json.format[PageAnalytics]
}

case class PageAnalytics (path: String, views: Int, uniqueViews: Int, avgTimeOnPage:Double,
                      entrances:Int, exits:Int, date:LocalDate)