package happiness

import daikon.lambda.HttpHandler

val happinessVoteUseCase = HappinessVoteUseCase(
    object : TextAppender {
        override fun append(text: String) {
            println(text)
        }
    }
)

class HappinessHandler(
    private val voteUseCase: HappinessVoteUseCase = happinessVoteUseCase
) : HttpHandler() {
    override fun routing() {
        post("/happiness/:vote") { req, res ->
            val vote = req.param(":vote").toInt()
            voteUseCase.execute(vote)

            res.status(201)
            res.write("Thanks for voting :D")
        }
    }
}