package happiness.uat

import happiness.BASE_URL
import happiness.infrastructure.KEY_NAME
import happiness.infrastructure.emptyBucketKey
import happiness.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.hamcrest.Matchers.hasItems
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import software.amazon.awssdk.services.s3.S3Client

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HappinessVoteAcceptanceTest {

    private val s3 by lazy { S3Client.create() }

    @BeforeEach
    fun setUp() {
        s3.emptyBucketKey("happiness-index-temp", KEY_NAME)
    }

    @Test
    fun `can give a happiness vote`() {
        post("$BASE_URL/happiness/1") shouldBe "Thanks for voting :D"
        post("$BASE_URL/happiness/2") shouldBe "Thanks for voting :D"

        get("$BASE_URL/happiness/votes")
            .body("votes.value", hasItems(1, 2))
    }

    private fun get(url: String): ValidatableResponse {
        return RestAssured
            .get(url)
            .then()
            .statusCode(200)
            .and()
            .contentType(ContentType.JSON)
    }

    private fun post(url: String): String {
        return RestAssured
            .post(url)
            .then()
            .statusCode(201)
            .extract()
            .body()
            .asString()
    }
}