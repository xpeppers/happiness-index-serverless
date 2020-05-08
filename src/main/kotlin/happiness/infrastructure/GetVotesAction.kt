package happiness.infrastructure

import com.google.gson.JsonPrimitive
import daikon.core.Context
import daikon.core.Request
import daikon.core.Response
import daikon.core.RouteAction
import daikon.gson.Serializer
import daikon.gson.json
import happiness.GetHappinessVotesUseCase
import happiness.UserVote
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GetVotesAction(private val votesUseCase: GetHappinessVotesUseCase) : RouteAction {
    override fun handle(request: Request, response: Response, context: Context) {
        val votes = votesUseCase.execute()

        response.status(200)
        response.json(VotesResponse(votes), dateSerializer())
    }

    private fun dateSerializer(): Serializer<LocalDateTime> {
        return Serializer(LocalDateTime::class) { date: LocalDateTime, _, _ ->
            JsonPrimitive(
                date.format(
                    DateTimeFormatter.ISO_DATE_TIME
                )
            )
        }
    }

    private data class VotesResponse(val votes: List<UserVote>)
}