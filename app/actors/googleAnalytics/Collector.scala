package actors.googleAnalytics

import javax.inject.{Inject, Named}

import actors.googleAnalytics.Consumer.SaveGoogleAnalytics
import actors.googleAnalytics.Scraper.{ScrapGoogleAnalytics, FetchedGoogleAnalytics}
import akka.actor._
import dal.PageAnalyticsRepository
import models.GAResult

object Collector {
  def props = Props[Collector]

  case class Collect()
}

class Collector @Inject()(@Named("ga-scrapper-actor") googleAnalyticsScraper: ActorRef,
                          @Named("ga-consumer-actor") googleAnalyticsConsumer: ActorRef,
                          pageRepository: PageAnalyticsRepository) extends Actor with ActorLogging {
  import Collector._
  def receive = {
    case Collect =>
      log.info("Collecting google analytics")
      googleAnalyticsScraper ! ScrapGoogleAnalytics

    case FetchedGoogleAnalytics(gaResult:GAResult) =>
      log.info(s"Received statistics for ${gaResult.pageViews.size} pages" )
      googleAnalyticsConsumer ! SaveGoogleAnalytics(gaResult)
  }
}