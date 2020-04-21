package happiness.uat

import happiness.BASE_URL
import happiness.infrastructure.*
import happiness.shouldBe
import io.restassured.RestAssured
import io.restassured.RestAssured.get
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import software.amazon.awssdk.services.s3.S3Client

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HappinessVoteAcceptanceTest {

    private val s3 by lazy { S3Client.create() }

    @BeforeEach
    fun setUp() {
        s3.createBucketIfNotExists(BUCKET_NAME)
        s3.emptyBucketKey(BUCKET_NAME, KEY_NAME)
    }

    @Test
    fun `can give a happiness vote`() {
        post("$BASE_URL/happiness/1") shouldBe "Thanks for voting :D"

        assertThat(votes()).containsExactly("1")

        post("$BASE_URL/happiness/2") shouldBe "Thanks for voting :D"

        assertThat(votes()).containsExactly("1", "2")
    }

    @Test
    fun `can read all votes`() {
        get("$BASE_URL/happiness/votes")
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

    private fun votes() = s3.readFromBucket(BUCKET_NAME, KEY_NAME)
}