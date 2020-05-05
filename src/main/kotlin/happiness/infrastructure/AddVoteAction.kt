package happiness.infrastructure

import daikon.core.Context
import daikon.core.Request
import daikon.core.Response
import daikon.core.RouteAction
import daikon.gson.Deserializer
import daikon.gson.json
import happiness.addvote.AddHappinessVoteUseCase
import happiness.addvote.UserVote
import java.time.LocalDateTime

class AddVoteAction(private val addVoteUseCase: AddHappinessVoteUseCase) : RouteAction {

    override fun handle(request: Request, response: Response, context: Context) {

        val userVoteRequest = request.json<UserVoteRequest>(dateDeserializer())
        val userVote = mapFrom(userVoteRequest)
        addVoteUseCase.execute(userVote)

        response.status(201)
        response.write("Thanks for voting :D")
    }

    private fun dateDeserializer() =
        Deserializer(LocalDateTime::class) { element, _, _ -> LocalDateTime.parse(element.asString) }

    internal data class UserVoteRequest(
        val vote: Int,
        val userId: String,
        val location: String,
        val date: LocalDateTime?
    )

    private fun mapFrom(userVoteRequest: UserVoteRequest): UserVote = UserVote(
        userVoteRequest.vote,
        userVoteRequest.userId,
        userVoteRequest.location,
        userVoteRequest.date ?: LocalDateTime.now()
    )
}