package controllers

import java.time.LocalDate
import javax.inject._

import dal._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

class PageAnalyticsController @Inject() (repo: PageAnalyticsRepository, val messagesApi: MessagesApi)
                                 (implicit ec: ExecutionContext) extends Controller with I18nSupport{

  def listPageAnalytics(path: Option[String], date: Option[String]) = Action.async {
    val localDate = if(date.isDefined) Some(LocalDate.parse(date.get)) else None
  	repo.list(path,localDate).map { pages =>
      Ok(Json.toJson(pages))
    }
  }
}
