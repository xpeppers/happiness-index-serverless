package happiness.getvotes

import happiness.addvote.Votes
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GetHappinessVotesUseCaseTest {

    private val votes = mockk<Votes>()

    @Test
    fun `get all votes`() {
        every { votes.all() } returns listOf(
            Vote(3),
            Vote(1),
            Vote(1)
        )

        val useCase = GetHappinessVotesUseCase(votes)
        val allVotes = useCase.execute()

        assertThat(allVotes).containsExactlyInAnyOrder(Vote(1), Vote(1), Vote(3))
    }
}