package actors.googleAnalytics

import javax.inject.Inject

import akka.actor._
import dal.PageAnalyticsRepository
import models.{GAResult, PageAnalytics}

object Consumer {
  def props = Props[Consumer]

  case class SaveGoogleAnalytics(gaResult: GAResult)
  case class GoogleAnalyticsSaved()
}

class Consumer @Inject()(repo: PageAnalyticsRepository) extends Actor with ActorLogging {
  import Consumer._

  def receive = {
    case SaveGoogleAnalytics(gaResult: GAResult) =>
      log.debug("Save google analytics")

      gaResult.pageViews.map( pv =>
        PageAnalytics(pv.path,pv.views,pv.uniqueViews,pv.avgTimeOnPage,pv.entrances,pv.exits, gaResult.startDate)
      ).foreach( pa => repo.create(pa))
      log.debug("Google analytics saved")
      sender() ! GoogleAnalyticsSaved
  }
}