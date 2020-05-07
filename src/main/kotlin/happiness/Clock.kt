package happiness

import java.time.LocalDateTime

interface Clock {
    fun now(): LocalDateTime
}
