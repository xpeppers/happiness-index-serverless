package happiness

class HappinessVoteUseCase(private val textAppender: TextAppender) {
    fun execute(vote: Int) {
        textAppender.append(vote.toString())
    }
}