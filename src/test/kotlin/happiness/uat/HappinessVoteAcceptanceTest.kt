package happiness.uat

import happiness.BASE_URL
import happiness.infrastructure.KEY_NAME
import happiness.infrastructure.emptyBucketKey
import happiness.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.hamcrest.Matchers.hasItems
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
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
    @Disabled("to convert to the new way of expressing happiness")
    fun `can give a happiness vote`() {
        post("$BASE_URL/happiness/1") shouldBe "Thanks for voting :D"
        post("$BASE_URL/happiness/2") shouldBe "Thanks for voting :D"

        get("$BASE_URL/happiness/votes")
            .body("votes.value", hasItems(1, 2))
    }

    @Test
    fun `can give a happiness vote for a given user and location`() {
        val happiness = """{ 
            "vote": 1,
            "userId": "1234",
            "location": "MI",
            "date":"2011-03-02T00:00:00"
        }            
        """.trimIndent()

        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(happiness)
            .post("$BASE_URL/happiness")
            .then()
            .statusCode(201)
            .extract()
            .body()
            .asString() shouldBe "Thanks for voting :D"

        get("$BASE_URL/happiness/votes")
            .body("votes.vote", hasItems(1))
//            .body("votes.date", hasItems("2011-03-02T00:00:00")) <== TODO
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