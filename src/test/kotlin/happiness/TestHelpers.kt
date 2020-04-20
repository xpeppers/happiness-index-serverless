package happiness

import org.assertj.core.api.Assertions

internal infix fun Any?.shouldBe(value: Any?) {
    Assertions.assertThat(this).isEqualTo(value)
}