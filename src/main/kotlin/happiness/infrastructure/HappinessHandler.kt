package happiness.infrastructure

import com.google.gson.Gson
import daikon.gson.json
import daikon.lambda.HttpHandler
import daikon.lambda.LambdaCall
import happiness.addvote.UserVote
import happiness.addvote.AddHappinessVoteUseCase
import happiness.getvotes.GetHappinessVotesUseCase
import happiness.getvotes.Vote

val BUCKET_NAME = System.getenv("HAPPINESS_BUCKET_NAME")
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
        post("/happiness") { req, res ->
            val userVote = Gson().fromJson(req.body(), UserVote::class.java)
            addVoteUseCase.execute(userVote)

            res.status(201)
            res.write("Thanks for voting :D")
        }

        get("/happiness/votes") { req, res ->
            val votes = getVotesUseCase.execute()

            res.status(200)
            res.json(VotesResponse(votes))
        }
    }

    data class VotesResponse(
        val votes: List<Vote>
    )
}

