package happiness.getvotes

import happiness.addvote.Votes

class GetVotesUseCase(private val votes: Votes) {
    fun execute(): List<Vote> = votes.all()
}
