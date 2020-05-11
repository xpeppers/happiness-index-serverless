package happiness.infrastructure

import daikon.core.Request
import daikon.core.Response
import happiness.GetHappinessVotesUseCase
import happiness.UserVote
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.parse

class GetVotesActionTest {

    private val getVotesUseCase = mockk<GetHappinessVotesUseCase>()
    private val request = mockk<Request>()
    private val response = mockk<Response>(relaxed = true)

    @Test
    fun `responds with all the stored votes when asking for all votes`() {
        every { getVotesUseCase.execute() } returns listOf(
            UserVote(vote = 4, date = parse("2011-03-02T11:23:00"), location = "any", userId = "any"),
            UserVote(vote = 1, date = parse("2012-04-05T14:23:00"), location = "any", userId = "any")
        )

        GetVotesAction(getVotesUseCase).handle(request, response, DummyContext())

        verify { response.status(200) }
        verify {
            response.write(
                """{"votes":[""" +
                        """{"vote":4,"userId":"any","location":"any","date":"2011-03-02T11:23:00"},""" +
                        """{"vote":1,"userId":"any","location":"any","date":"2012-04-05T14:23:00"}""" +
                """]}"""
            )
        }
    }

//    @Test
//    fun `responds with a single vote matching the time range query`() {
//        every { request.param("from") } returns "2020-03-03"
//        every { request.param("to") } returns "2020-04-05"
//
//        every { getVotesUseCase.execute(SearchVotesQuery(from = "2020-03-03", to = "2020-04-05")) } returns listOf(
//            UserVote(vote = 1, date = parse("2011-03-03T11:23:00"), location = "any", userId = "any")
//        )
//
//        GetVotesAction(getVotesUseCase).handle(request, response, DummyContext())
//
//        verify { response.write("""{"votes":[{"vote":1,"userId":"any","location":"any","date":"2011-03-03T11:23:00"}]}""") }
//    }

}

