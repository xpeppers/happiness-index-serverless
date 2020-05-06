package happiness.infrastructure

import java.time.LocalDateTime

class RealClock : Clock {
    override fun now(): LocalDateTime = LocalDateTime.now()
}