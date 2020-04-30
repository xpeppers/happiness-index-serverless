package happiness.getvotes

import happiness.addvote.UserVote
import happiness.addvote.Votes

class GetHappinessVotesUseCase(private val votes: Votes) {
    fun execute(): List<UserVote> = votes.all()
}
