package happiness

class GetHappinessVotesUseCase(private val votes: Votes) {
    fun execute(searchCriteria: SearchCriteria): List<UserVote> {
        return votes.all()
            .filter { it.isAfter(searchCriteria.startingDate) }
            .filter { it.isBefore(searchCriteria.endingDate) }
    }
}
