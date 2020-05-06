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

class AddVoteAction(
    private val addVoteUseCase: AddHappinessVoteUseCase,
    private val clock: Clock
) : RouteAction {

    override fun handle(request: Request, response: Response, context: Context) {
        val userVote = request.json<UserVote>(userVoteDeserializer())

        addVoteUseCase.execute(userVote)

        response.status(201)
        response.write("Thanks for voting :D")
    }

    private fun userVoteDeserializer() = Deserializer(UserVote::class) { element, _, _ ->
        val jsonObject = element.asJsonObject
        UserVote(
            jsonObject.get("vote").asInt,
            jsonObject.get("userId").asString,
            jsonObject.get("location").asString,
            jsonObject.get("date")?.let { LocalDateTime.parse(it.asString) } ?: clock.now()
        )
    }
}