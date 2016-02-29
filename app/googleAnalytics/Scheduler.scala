package googleAnalytics

import javax.inject.{Named, Inject}

import actors.googleAnalytics.Collector
import Collector.Collect
import akka.actor.{ActorRef, ActorSystem}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

class Scheduler @Inject()(val system: ActorSystem, @Named("ga-collector-actor") val gaCollector: ActorRef)(implicit ec: ExecutionContext) {
  system.scheduler.schedule(0.microseconds, 15.minutes, gaCollector, Collect)
}