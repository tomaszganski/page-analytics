package dal

import java.time.LocalDate
import javax.inject.{Inject, Singleton}

import models.PageAnalytics
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PageAnalyticsRepository @Inject()(dbConfigProvider: DatabaseConfigProvider) (implicit ec: ExecutionContext)  {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class PageAnalyticsTable(tag: Tag) extends Table[PageAnalytics](tag, "page_analytics") {
    implicit val localDateColumnType = MappedColumnType.base[LocalDate, java.sql.Date](
      {localDate => java.sql.Date.valueOf(localDate)},
      {sqlDate => sqlDate.toLocalDate}
    )

    def path = column[String]("path")

    def views = column[Int]("views")
    def uniqueViews = column[Int]("unique_views")
    def avgTimeOnPage = column[Double]("avg_time_on_page")
    def entrances = column[Int]("entrances")
    def exits = column[Int]("exits")
    def date = column[LocalDate]("date")
    def pk = primaryKey("pk_a", (path, date))
    def * = (path, views, uniqueViews, avgTimeOnPage, entrances, exits, date) <> ((PageAnalytics.apply _).tupled, PageAnalytics.unapply)
  }

  private val pageAnalytics = TableQuery[PageAnalyticsTable]

  def create(page: PageAnalytics) = db.run {
    implicit val localDateColumnType = MappedColumnType.base[LocalDate, java.sql.Date](
      {localDate => java.sql.Date.valueOf(localDate)},
      {sqlDate => sqlDate.toLocalDate}
    )
    pageAnalytics
      .map(p => (p.path, p.views, p.uniqueViews, p.avgTimeOnPage, p.entrances, p.exits, p.date)
    ).insertOrUpdate(page.path, page.views, page.uniqueViews, page.avgTimeOnPage, page.entrances, page.exits, page.date)
  }

  def list(): Future[Seq[PageAnalytics]] = db.run {
    pageAnalytics.result
  }

  def list(pathFilter: Option[String], dateFilter: Option[LocalDate]): Future[Seq[PageAnalytics]] = db.run {
    implicit val localDateColumnType = MappedColumnType.base[LocalDate, java.sql.Date](
      {localDate => java.sql.Date.valueOf(localDate)},
      {sqlDate => sqlDate.toLocalDate}
    )

    val pages = pageAnalytics.sortBy(_.date.desc)
    val filterPages = pathFilter match {
      case Some(path) => pages.filter (_.path === path)
      case None =>  pages
    }

    dateFilter match {
      case Some(date) => filterPages.filter (_.date === date).result
      case None =>  filterPages.result
    }
  }
}
