package models

import java.time.LocalDate


case class GAResult(profileName: String, startDate: LocalDate, endDate: LocalDate, pageViews: List[PageViews])
