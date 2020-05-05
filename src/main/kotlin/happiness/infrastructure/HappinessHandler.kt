package happiness.infrastructure

import com.google.gson.JsonPrimitive
import daikon.gson.Deserializer
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
        post("/happiness") { request, response ->
            val userVoteRequest = request.json<UserVoteRequest>(dateDeserializer())
            val userVote = mapFrom(userVoteRequest)
            addVoteUseCase.execute(userVote)

            response.status(201)
            response.write("Thanks for voting :D")
        }

        get("/happiness/votes") { _, response ->
            val votes = getVotesUseCase.execute()

            response.status(200)
            response.json(VotesResponse(votes), dateSerializer())
        }
    }

    private fun mapFrom(userVoteRequest: UserVoteRequest): UserVote {
        return UserVote(
            userVoteRequest.vote,
            userVoteRequest.userId,
            userVoteRequest.location,
            userVoteRequest.date ?: LocalDateTime.now()
        )
    }

    private fun dateSerializer(): Serializer<LocalDateTime> {
        return Serializer(LocalDateTime::class) { date: LocalDateTime, _, _ -> JsonPrimitive(date.format(ISO_DATE_TIME)) }
    }

    private fun dateDeserializer() =
        Deserializer(LocalDateTime::class) { element, _, _ -> LocalDateTime.parse(element.asString) }

    private data class VotesResponse(val votes: List<UserVote>)

    internal data class UserVoteRequest(
        val vote: Int,
        val userId: String,
        val location: String,
        val date: LocalDateTime?
    )
}


