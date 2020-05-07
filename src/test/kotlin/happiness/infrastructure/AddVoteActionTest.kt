package happiness.infrastructure

import daikon.core.Context
import daikon.core.Request
import daikon.core.Response
import happiness.AddHappinessVoteUseCase
import happiness.FakeClock
import happiness.UserVote
import io.mockk.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.parse

class AddVoteActionTest {

    private val addVoteUseCase = mockk<AddHappinessVoteUseCase>()
    private val request = mockk<Request>()
    private val response = mockk<Response>(relaxed = true)

    @Test
    fun `add user vote with the given date`() {
        val jsonRequest = """{ 
            "vote": 3,
            "userId": "4422",
            "location": "RM",
            "date":"2020-03-02T13:23:00"
        }            
        """
        every { addVoteUseCase.execute(any()) } just Runs
        every { request.body() } returns jsonRequest

        AddVoteAction(addVoteUseCase, FakeClock("1999-12-00T53:42:22")).handle(request, response, dummyContext())

        verify {
            addVoteUseCase.execute(
                UserVote(
                    3,
                    "4422",
                    "RM",
                    parse("2020-03-02T13:23:00")
                )
            )
        }
    }

    @Test
    fun `add user vote using currentDate when date is not given`() {
        val jsonRequest = """{ 
            "vote": 3,
            "userId": "4422",
            "location": "RM"
        }            
        """
        every { addVoteUseCase.execute(any()) } just Runs
        every { request.body() } returns jsonRequest

        AddVoteAction(addVoteUseCase, FakeClock("1496-12-02T02:11:01")).handle(request, response, dummyContext())

        verify {
            addVoteUseCase.execute(
                UserVote(
                    3,
                    "4422",
                    "RM",
                    parse("1496-12-02T02:11:01")
                )
            )
        }
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