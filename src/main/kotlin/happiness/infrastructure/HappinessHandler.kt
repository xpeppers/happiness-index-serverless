package happiness.infrastructure

import daikon.lambda.HttpHandler
import happiness.addvote.HappinessVoteUseCase
import happiness.addvote.Votes

val happinessVoteUseCase = HappinessVoteUseCase(
    object : Votes {
        override fun add(vote: String) {
            println(vote)
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