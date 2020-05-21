package happiness

class GetHappinessVotesUseCase(private val votes: Votes) {
    fun execute(searchCriteria: SearchCriteria): List<UserVote> {
        return allVotes()
            .filter { it.isAfter(searchCriteria.startingDate) }
            .filter { it.isBefore(searchCriteria.endingDate) }
    }

    fun execute(): List<UserVote> = allVotes()

    private fun allVotes() = votes.all()
}
