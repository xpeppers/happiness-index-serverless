package happiness

class GetHappinessVotesUseCase(private val votes: Votes) {
    fun execute(): List<UserVote> = votes.all()
}
