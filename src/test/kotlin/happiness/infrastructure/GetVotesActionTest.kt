package happiness.infrastructure

import daikon.core.Request
import daikon.core.Response
import happiness.GetHappinessVotesUseCase
import happiness.UserVote
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class GetVotesActionTest {

    private val getVotesUseCase = mockk<GetHappinessVotesUseCase>()
    private val request = mockk<Request>()
    private val response = mockk<Response>(relaxed = true)

    @Test
    fun `responds with all the stored votes when asking for all votes`() {
        every { getVotesUseCase.execute() } returns listOf(
            UserVote(
                vote = 1,
                date = LocalDateTime.parse("2011-03-02T11:23:00"),
                location = "any",
                userId = "any"
            )
        )

        GetVotesAction(getVotesUseCase).handle(request, response, DummyContext())

        verify { response.status(200) }
        verify { response.write("""{"votes":[{"vote":1,"userId":"any","location":"any","date":"2011-03-02T11:23:00"}]}""") }
    }
}

