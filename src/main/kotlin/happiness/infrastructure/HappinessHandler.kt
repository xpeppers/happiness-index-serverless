package happiness.infrastructure

import daikon.lambda.HttpHandler
import happiness.addvote.HappinessVoteUseCase
import happiness.addvote.Votes
import java.util.*

const val BUCKET_NAME = "happiness-index-pietro-dibello"
const val KEY_NAME = "votes"

val happinessVoteUseCase = HappinessVoteUseCase(
    VotesOnS3(BUCKET_NAME,KEY_NAME)
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