package happiness.uat

import happiness.BASE_URL
import happiness.infrastructure.KEY_NAME
import happiness.infrastructure.emptyBucketKey
import happiness.shouldBe
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.hamcrest.Matchers.hasItems
import org.hamcrest.Matchers.not
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
    fun `can give a happiness vote for a given user and location`() {
        val userVote = """{ 
            "vote": 1,
            "userId": "1234",
            "location": "MI",
            "date":"2020-03-03T15:13:10"
        }            
        """

        val anotherUserVote = """{ 
            "vote": 3,
            "userId": "4422",
            "location": "RM",
            "date":"2020-03-02T13:23:00"
        }            
        """

        post("$BASE_URL/happiness", userVote) shouldBe "Thanks for voting :D"
        post("$BASE_URL/happiness", anotherUserVote) shouldBe "Thanks for voting :D"

        get("$BASE_URL/happiness/votes")
            .body("votes.vote", hasItems(1, 3))
            .body("votes.date", hasItems("2020-03-03T15:13:10", "2020-03-02T13:23:00"))
            .body("votes.userId", hasItems("1234", "4422"))
            .body("votes.location", hasItems("MI", "RM"))
    }

    @Test
    fun `we can search all votes in given time range`() {
        addVotes(
            """{ 
                    "vote": 1,
                    "userId": "1234",
                    "location": "MI",
                    "date":"2020-03-03T15:13:10"
                }            
        """, """{ 
                    "vote": 3,
                    "userId": "4422",
                    "location": "RM",
                    "date":"2020-03-02T13:23:00"
                }            
        """
        )

        get("$BASE_URL/happiness/votes?from=2020-03-03&to=2020-03-03")
            .body("votes.vote", not(hasItems(3)))
            .body("votes.vote", hasItems(1))
    }

    private fun addVotes(vararg userVotes: String) {
        userVotes.forEach { post("$BASE_URL/happiness", it) shouldBe "Thanks for voting :D" }
    }

    private fun post(url: String, json: String): String? {
        return RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(json)
            .post(url)
            .then()
            .statusCode(201)
            .extract()
            .body()
            .asString()
    }

    private fun get(url: String): ValidatableResponse {
        return RestAssured
            .get(url)
            .then()
            .statusCode(200)
            .and()
            .contentType(ContentType.JSON)
    }
}