package googleAnalytics

import actors.googleAnalytics.{Scraper, Consumer, Collector}
import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

class GoogleAnalyticsModule extends AbstractModule with AkkaGuiceSupport {
  def configure = {
    bindActor[Collector]("ga-collector-actor")
    bindActor[Scraper]("ga-scrapper-actor")
    bindActor[Consumer]("ga-consumer-actor")
    bind(classOf[Scheduler]).asEagerSingleton()
  }
}