package happiness.infrastructure

import io.restassured.RestAssured.post
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HappinessHandlerTestIT {

    @Test
    fun `route respond correctly`() {
        post("$BASE_URL/happiness/1")
            .then()
            .statusCode(201)
            .extract()
            .body()
            .asString() shouldBe "Thanks for voting :D"
    }

    companion object {
        private const val BASE_URL = "https://g49lpxwuhd.execute-api.eu-west-1.amazonaws.com/dev"
    }
}

private infix fun Any?.shouldBe(value: Any?) {
    assertThat(this).isEqualTo(value)
}
