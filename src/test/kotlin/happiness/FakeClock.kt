package happiness

import java.time.LocalDateTime

class FakeClock(private val date: String) : Clock {
    override fun now(): LocalDateTime = LocalDateTime.parse(date)
}
