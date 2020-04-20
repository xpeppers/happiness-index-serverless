package happiness.getvotes

import happiness.addvote.Votes

class GetHappinessVotesUseCase(private val votes: Votes) {
    fun execute(): List<Vote> = votes.all()
}
