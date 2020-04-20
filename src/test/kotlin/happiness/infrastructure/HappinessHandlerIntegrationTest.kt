package happiness.infrastructure

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import happiness.BASE_URL
import happiness.getvotes.Vote
import happiness.shouldBe
import io.restassured.RestAssured.get
import io.restassured.RestAssured.post
import org.junit.jupiter.api.Test

class HappinessHandlerIntegrationTest {

    @Test
    fun `add vote route respond correctly`() {
        post("$BASE_URL/happiness/1")
            .then()
            .statusCode(201)
            .extract()
            .body()
            .asString() shouldBe "Thanks for voting :D"
    }

    @Test
    fun `get votes route respond correctly`() {
        val response = get("$BASE_URL/happiness/votes")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val gson = Gson()
        val itemType = object : TypeToken<List<Vote>>() {}.type

        gson.fromJson<List<Vote>>(response, itemType)
    }

}

