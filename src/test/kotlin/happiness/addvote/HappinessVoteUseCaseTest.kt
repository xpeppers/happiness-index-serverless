package happiness.addvote

import io.mockk.*
import org.junit.jupiter.api.Test

class HappinessVoteUseCaseTest {

    private val votes = mockk<Votes>()
    private val voteUseCase = HappinessVoteUseCase(votes)

    @Test
    fun `useCase call properly the votes repository`() {
        every { votes.add(any()) } just Runs
        voteUseCase.execute(1)

        verify { votes.add("1") }
        confirmVerified(votes)
    }
}