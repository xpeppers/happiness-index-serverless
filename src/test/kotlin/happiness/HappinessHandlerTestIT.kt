package happiness

import io.restassured.RestAssured.post
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

class HappinessHandlerTestIT {

    @Test
    fun `route respond correctly`() {
        val response = post("$URL/1")
            .then()
            .statusCode(201)
            .extract()
            .body().asByteArray().toString(Charset.defaultCharset())

        assertThat(response).isEqualTo("Thanks for voting :D")
    }

    companion object {
        private const val URL = "https://qxzc0sz9i8.execute-api.eu-west-1.amazonaws.com/dev/happiness"
    }
}