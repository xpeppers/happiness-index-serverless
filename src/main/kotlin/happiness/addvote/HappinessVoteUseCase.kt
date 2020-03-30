package happiness.addvote

class HappinessVoteUseCase(private val votes: Votes) {
    fun execute(vote: Int) {
        votes.add(vote.toString())
    }
}