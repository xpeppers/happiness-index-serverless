package happiness

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime.parse

class GetHappinessVotesUseCaseTest {

    private val votes = mockk<Votes>()
    private val getHappinessVotes = GetHappinessVotesUseCase(votes)

    @Test
    fun `get all votes starting from a date`() {
        every { votes.all() } returns listOf(
            UserVote(vote = 1, date = parse("2011-03-02T11:23:00"), location = "any", userId = "any"),
            UserVote(vote = 4, date = parse("2011-03-03T12:25:00"), location = "any", userId = "any"),
            UserVote(vote = 3, date = parse("2011-04-02T13:00:00"), location = "any", userId = "any")
        )

        val allVotes = getHappinessVotes.execute(SearchCriteria(fromDate = "2011-03-03"))

        assertThat(allVotes).containsExactlyInAnyOrder(
            UserVote(vote = 4, date = parse("2011-03-03T12:25:00"), location = "any", userId = "any"),
            UserVote(vote = 3, date = parse("2011-04-02T13:00:00"), location = "any", userId = "any")
        )
    }

    @Test
    fun `get all votes up to a date`() {
        every { votes.all() } returns listOf(
            UserVote(vote = 1, date = parse("2011-03-02T11:23:00"), location = "any", userId = "any"),
            UserVote(vote = 4, date = parse("2011-03-03T12:25:00"), location = "any", userId = "any"),
            UserVote(vote = 3, date = parse("2011-04-02T13:00:00"), location = "any", userId = "any")
        )

        val allVotes = getHappinessVotes.execute(SearchCriteria(toDate = "2011-03-03"))

        assertThat(allVotes).containsExactlyInAnyOrder(
            UserVote(vote = 1, date = parse("2011-03-02T11:23:00"), location = "any", userId = "any"),
            UserVote(vote = 4, date = parse("2011-03-03T12:25:00"), location = "any", userId = "any")
        )
    }

    @Test
    fun `get all votes`() {
        every { votes.all() } returns listOf(
            UserVote(vote = 1, date = parse("2011-03-02T11:23:00"), location = "any", userId = "any"),
            UserVote(vote = 4, date = parse("2011-03-03T12:25:00"), location = "any", userId = "any"),
            UserVote(vote = 3, date = parse("2011-04-02T13:00:00"), location = "any", userId = "any")
        )

        val allVotes = getHappinessVotes.execute(SearchCriteria())

        assertThat(allVotes).containsExactlyInAnyOrder(
            UserVote(vote = 1, date = parse("2011-03-02T11:23:00"), location = "any", userId = "any"),
            UserVote(vote = 4, date = parse("2011-03-03T12:25:00"), location = "any", userId = "any"),
            UserVote(vote = 3, date = parse("2011-04-02T13:00:00"), location = "any", userId = "any")
        )
    }
}