package happiness.addvote

class AddHappinessVoteUseCase(private val votes: Votes) {
    fun execute(vote: Int) {
        votes.add(vote.toString())
    }
}