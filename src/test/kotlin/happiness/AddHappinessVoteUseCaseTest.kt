package happiness

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.parse

class AddHappinessVoteUseCaseTest {

    private val votes = mockk<Votes>(relaxed = true)
    private val addHappinessVote = AddHappinessVoteUseCase(votes)

    @Test
    fun `add the vote to the stored votes collection`() {
        val vote = UserVote(vote = 1, userId = "1234", location = "MI", date = parse("2020-05-01T11:23:23"))

        addHappinessVote.execute(vote)

        verify { votes.add(vote) }
    }
}