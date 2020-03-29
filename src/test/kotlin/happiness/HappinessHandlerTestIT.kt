package happiness

import io.restassured.RestAssured.get
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.nio.charset.Charset

class HappinessHandlerTestIT {

    @Test
    fun `route respond correctly`() {
        val response = get(URL)
            .then()
            .statusCode(200)
            .extract()
            .body().asByteArray().toString(Charset.defaultCharset())

        assertThat(response).isEqualTo("Hello world")
    }

    companion object {
        private const val URL = "https://qxzc0sz9i8.execute-api.eu-west-1.amazonaws.com/dev/"
    }
}