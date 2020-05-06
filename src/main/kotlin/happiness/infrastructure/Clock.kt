package happiness.infrastructure

import java.time.LocalDateTime

interface Clock {
    fun now(): LocalDateTime
}
