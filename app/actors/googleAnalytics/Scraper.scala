package actors.googleAnalytics

import java.io.File
import java.time.LocalDate
import javax.inject.Inject

import akka.actor._
import com.google.api.services.analytics.model.GaData
import com.google.api.services.analytics.Analytics
import domain.AnalyticsConnectionFactory
import models.{GAResult, PageViews}
import play.Configuration

import scala.collection.JavaConversions._

object Scraper {
  def props = Props[Scraper]

  case class ScrapGoogleAnalytics()
  case class FetchedGoogleAnalytics(gaResult: GAResult)
}

class Scraper @Inject()(configuration: Configuration, analyticsConnectionFactory: AnalyticsConnectionFactory) extends Actor with ActorLogging {
  import Scraper._

  val serviceAccountId = configuration.getString("ga.service-account-email")
  val keyFileLocation = configuration.getString("ga.p12-key-file-location")
  val profileId = configuration.getString("ga.profile-id")
  val applicationName = configuration.getString("ga.application-name")
  val queryFilters =  configuration.getString("ga.query-filters")
  val queryDimensions = "ga:pagePath"
  val querySort = "-ga:pageviews"
  val queryMetrics = "ga:pageviews,ga:uniquePageviews,ga:avgTimeOnPage,ga:entrances,ga:exits"

  def receive = {
    case ScrapGoogleAnalytics =>
      log.debug("Scrap google analytics")
      val analytics: Analytics = analyticsConnectionFactory.createAnalytics(serviceAccountId, keyFileLocation, applicationName)

      log.debug("Fetch statistics for pages")
      val results: GaData = getTodayData(profileId, queryFilters, queryDimensions, querySort, queryMetrics, analytics)
      log.debug("Statistics fetched")

      if (results != null && !results.getRows.isEmpty) {
        sender() ! FetchedGoogleAnalytics(mapGaResult(results))
      }
  }

  def getTodayData(profileId: String, queryFilters: String, queryDimensions: String, querySort: String, queryMetrics: String, analytics: Analytics): GaData = {
    val startDate: LocalDate = LocalDate.now()
    val endDate: LocalDate = LocalDate.now()
    analytics.data.ga.get("ga:" + profileId, startDate.toString, endDate.toString,
      queryMetrics)
      .setDimensions(queryDimensions)
      .setSort(querySort)
      .setFilters(queryFilters)
      .execute
  }

  def mapGaResult(gaData: GaData): GAResult = {
    val pageViews: List[PageViews] = gaData.getRows.toList.map ( row =>
      PageViews(row.get(0),row.get(1).toInt,row.get(2).toInt,row.get(3).toDouble,row.get(4).toInt, row.get(5).toInt)
    )
    val starDate: LocalDate = LocalDate.parse(gaData.getQuery.getStartDate)
    val endDate: LocalDate = LocalDate.parse(gaData.getQuery.getEndDate)
    GAResult(gaData.getProfileInfo.getProfileName, starDate, endDate, pageViews)
  }
}