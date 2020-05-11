package happiness

import java.time.LocalDate

data class SearchCriteria(private val fromDate: String = "", private val toDate: String = "") {
    val startingDate: LocalDate = if (fromDate.isEmpty()) LocalDate.MIN else LocalDate.parse(fromDate)
    val endingDate: LocalDate =  if (toDate.isEmpty()) LocalDate.MAX else LocalDate.parse(toDate)

}