package happiness.addvote

import java.time.LocalDateTime

data class UserVote(
    val vote: Int,
    val userId: String,
    val location: String,
    val date: LocalDateTime = LocalDateTime.now()
)