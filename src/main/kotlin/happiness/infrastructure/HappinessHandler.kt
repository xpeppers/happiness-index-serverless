package happiness.infrastructure

import daikon.gson.Deserializer
import daikon.gson.json
import daikon.lambda.HttpHandler
import daikon.lambda.LambdaCall
import happiness.addvote.UserVote
import happiness.addvote.AddHappinessVoteUseCase
import happiness.getvotes.GetHappinessVotesUseCase
import java.time.LocalDateTime

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
        exception(Throwable::class.java) { _, _, t -> t.printStackTrace() }
        post("/happiness") { req, res ->
            val userVote = req.json<UserVote>(dateDeserializer())
            addVoteUseCase.execute(userVote)

            res.status(201)
            res.write("Thanks for voting :D")
        }

        get("/happiness/votes") { _, res ->
            val votes = getVotesUseCase.execute()

            res.status(200)
            res.json(VotesResponse(votes))
        }
    }

    private fun dateDeserializer() =
        Deserializer(LocalDateTime::class) { element, _, _ -> LocalDateTime.parse(element.asString) }

    private data class VotesResponse(val votes: List<UserVote>)
}
