package happiness

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class UserVote(
    val vote: Int,
    val userId: String,
    val location: String,
    val date: LocalDateTime = LocalDateTime.now()
) {
    fun isAfter(startingDate: LocalDate) = date.isAfter(startingDate.atStartOfDay())
    fun isBefore(endingDate: LocalDate) = date.isBefore(endingDate.atEndOfDay())
}

private fun LocalDate.atEndOfDay() = LocalDateTime.of(this, LocalTime.MAX)
