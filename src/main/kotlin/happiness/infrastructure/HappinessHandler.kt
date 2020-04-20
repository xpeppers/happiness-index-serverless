package happiness.infrastructure

import daikon.gson.json
import daikon.lambda.HttpHandler
import happiness.addvote.VoteUseCase
import happiness.getvotes.GetVotesUseCase

const val BUCKET_NAME = "happiness-index-pietro-angelo"
const val KEY_NAME = "votes"

val happinessVoteUseCase = VoteUseCase(
    VotesOnS3(BUCKET_NAME, KEY_NAME)
)

val happinessGetVotesUseCase = GetVotesUseCase(
    VotesOnS3(BUCKET_NAME, KEY_NAME)
)

class HappinessHandler(
    private val voteUseCase: VoteUseCase = happinessVoteUseCase,
    private val getVotesUseCase: GetVotesUseCase = happinessGetVotesUseCase
) : HttpHandler() {
    override fun routing() {
        post("/happiness/:vote") { req, res ->
            val vote = req.param(":vote").toInt()
            voteUseCase.execute(vote)

            res.status(201)
            res.write("Thanks for voting :D")
        }

        get("/happiness/votes") { req, res ->
            val votes = happinessGetVotesUseCase.execute()

            res.status(200)
            res.json(votes)
        }
    }
}