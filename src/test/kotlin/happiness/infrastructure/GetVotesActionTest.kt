package happiness.infrastructure

import daikon.core.Request
import daikon.core.Response
import happiness.GetHappinessVotesUseCase
import happiness.SearchCriteria
import happiness.UserVote
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class GetVotesActionTest {

    private val getVotesUseCase = mockk<GetHappinessVotesUseCase>(relaxed = true)
    private val request = mockk<Request>(relaxed = true)
    private val response = mockk<Response>(relaxed = true)

    @Test
    fun `get the votes from the underlying use case`() {
        every { request.hasParam(any()) } returns true
        every { request.param("from") } returns "2020-03-01"
        every { request.param("to") } returns "2020-03-05"

        GetVotesAction(getVotesUseCase).handle(request, response, DummyContext())

        verify { getVotesUseCase.execute(SearchCriteria(fromDate = "2020-03-01", toDate = "2020-03-05")) }
    }

    @Test
    fun `renders the votes as json`() {
        every { getVotesUseCase.execute() } returns listOf(
            UserVote(vote = 4, date = LocalDateTime.parse("2020-03-02T11:23:00"), location = "any", userId = "any"),
            UserVote(vote = 1, date = LocalDateTime.parse("2012-04-05T14:23:00"), location = "any", userId = "any")
        )

        GetVotesAction(getVotesUseCase).handle(request, response, DummyContext())

        verify { response.status(200) }
        verify { response.header("Access-Control-Allow-Origin", "*") }
        verify {
            response.write(
                """{"votes":[""" +
                        """{"vote":4,"userId":"any","location":"any","date":"2020-03-02T11:23:00"},""" +
                        """{"vote":1,"userId":"any","location":"any","date":"2012-04-05T14:23:00"}""" +
                        """]}"""
            )
        }
    }

}

