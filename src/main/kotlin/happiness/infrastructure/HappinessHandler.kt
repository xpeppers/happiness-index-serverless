package happiness.infrastructure

import daikon.gson.json
import daikon.lambda.HttpHandler
import daikon.lambda.LambdaCall
import happiness.addvote.AddHappinessVoteUseCase
import happiness.getvotes.GetHappinessVotesUseCase

const val BUCKET_NAME = "happiness-index-pietro-angelo"
const val KEY_NAME = "votes"

val addHappinessVote = AddHappinessVoteUseCase(
    VotesOnS3(BUCKET_NAME, KEY_NAME)
)

val getHappinessVotes = GetHappinessVotesUseCase(
    VotesOnS3(BUCKET_NAME, KEY_NAME)
)

class HappinessHandler(
    private val addVoteUseCase: AddHappinessVoteUseCase = addHappinessVote,
    private val getVotesUseCase: GetHappinessVotesUseCase = getHappinessVotes
) : HttpHandler() {
    override fun LambdaCall.routing() {
        post("/happiness/:vote") { req, res ->
            val vote = req.param(":vote").toInt()
            addVoteUseCase.execute(vote)

            res.status(201)
            res.write("Thanks for voting :D")
        }

        get("/happiness/votes") { req, res ->
            val votes = getVotesUseCase.execute()

            res.status(200)
            res.json(votes)
        }
    }
}