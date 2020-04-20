package happiness.addvote

class VoteUseCase(private val votes: Votes) {
    fun execute(vote: Int) {
        votes.add(vote.toString())
    }
}