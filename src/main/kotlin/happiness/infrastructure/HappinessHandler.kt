package happiness.infrastructure

import com.google.gson.JsonPrimitive
import daikon.gson.Serializer
import daikon.gson.json
import daikon.lambda.HttpHandler
import daikon.lambda.LambdaCall
import happiness.addvote.AddHappinessVoteUseCase
import happiness.addvote.UserVote
import happiness.getvotes.GetHappinessVotesUseCase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME

val BUCKET_NAME: String = System.getenv("HAPPINESS_BUCKET_NAME")
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
        post("/happiness", AddVoteAction(addVoteUseCase))

        get("/happiness/votes") { _, response ->
            val votes = getVotesUseCase.execute()

            response.status(200)
            response.json(VotesResponse(votes), dateSerializer())
        }
    }

    private fun dateSerializer(): Serializer<LocalDateTime> {
        return Serializer(LocalDateTime::class) { date: LocalDateTime, _, _ -> JsonPrimitive(date.format(ISO_DATE_TIME)) }
    }

    private data class VotesResponse(val votes: List<UserVote>)
}


