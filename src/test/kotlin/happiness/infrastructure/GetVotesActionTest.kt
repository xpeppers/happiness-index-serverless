package happiness.infrastructure

import daikon.core.Request
import daikon.core.Response
import happiness.GetHappinessVotesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class GetVotesActionTest {

    private val getVotesUseCase = mockk<GetHappinessVotesUseCase>()
    private val request = mockk<Request>()
    private val response = mockk<Response>(relaxed = true)

    @Test
    fun `responds with HTTP 200 when asking for votes`() {
        every { getVotesUseCase.execute() } returns emptyList()

        GetVotesAction(getVotesUseCase).handle(request, response, DummyContext())

        verify { response.status(200) }
    }
}

