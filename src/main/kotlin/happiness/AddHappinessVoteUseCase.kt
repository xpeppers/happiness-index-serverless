package happiness

class AddHappinessVoteUseCase(private val votes: Votes) {
    fun execute(vote: UserVote) {
        votes.add(vote)
    }
}


