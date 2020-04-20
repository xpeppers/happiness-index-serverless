package happiness.addvote

import io.mockk.*
import org.junit.jupiter.api.Test

class AddHappinessVoteUseCaseTest {

    private val votes = mockk<Votes>()
    private val addHappinessVote = AddHappinessVoteUseCase(votes)

    @Test
    fun `useCase call properly the votes repository`() {
        every { votes.add(any()) } just Runs
        addHappinessVote.execute(1)

        verify { votes.add("1") }
        confirmVerified(votes)
    }
}