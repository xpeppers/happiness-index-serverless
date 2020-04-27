package happiness.addvote

import io.mockk.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.Month

class AddHappinessVoteUseCaseTest {

    private val votes = mockk<Votes>()
    private val addHappinessVote = AddHappinessVoteUseCase(votes)

    @Test
    fun `useCase call properly the votes repository`() {
        every { votes.add(any()) } just Runs

        val vote = UserVote(
            vote = 1,
            userId = "1234",
            location = "MI",
            date = LocalDateTime.of(2020, Month.APRIL, 23, 11, 39)
        )
        addHappinessVote.execute(vote)

        verify { votes.add(vote) }
        confirmVerified(votes)
    }
}