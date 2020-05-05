package happiness.infrastructure

import daikon.core.Context
import daikon.core.Request
import daikon.core.Response
import happiness.addvote.AddHappinessVoteUseCase
import happiness.addvote.UserVote
import io.mockk.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.parse

class AddVoteActionTest {

    private val addVoteUseCase = mockk<AddHappinessVoteUseCase>()
    private val action = AddVoteAction(addVoteUseCase)
    private val request = mockk<Request>()
    private val response = mockk<Response>(relaxed = true)

    @Test
    fun `execute the add vote use case with given user vote json request`() {
        val jsonRequest = """{ 
            "vote": 3,
            "userId": "4422",
            "location": "RM",
            "date":"2020-03-02T13:23:00"
        }            
        """
        every { addVoteUseCase.execute(any()) } just Runs
        every { request.body() } returns jsonRequest

        action.handle(request, response, dummyContext())

        verify { addVoteUseCase.execute(UserVote(3, "4422", "RM", parse("2020-03-02T13:23:00"))) }
    }

    private fun dummyContext(): Context {
        return object : Context {
            override fun addAttribute(key: String, value: Any) {}
            override fun <T> getAttribute(key: String): T {
                throw NotImplementedError()
            }
        }
    }
}