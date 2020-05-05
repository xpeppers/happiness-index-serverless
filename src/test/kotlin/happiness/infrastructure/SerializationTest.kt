package happiness.infrastructure

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.LocalDateTime.parse

class SerializationTest {
    @Test
    fun `deserializes date correctly`() {
        val json = """{ 
            "vote": 3,
            "userId": "4422",
            "location": "RM",
            "date":"2020-03-02T13:23:00"
        }            
        """

        val parsed = gson().fromJson(json, HappinessHandler.UserVoteRequest::class.java)

        assertThat(parsed.date).isEqualTo(parse("2020-03-02T13:23:00"))
    }

    @Test
    fun `deserializes an empty date`() {
        val json = """{ 
            "vote": 3,
            "userId": "4422",
            "location": "RM"
        }            
        """

        val parsed = gson().fromJson(json, HappinessHandler.UserVoteRequest::class.java)

        assertThat(parsed.date).isNull()
    }

    private fun gson() =
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer { element, _, _ -> parse(element.asString) })
            .create()
}
