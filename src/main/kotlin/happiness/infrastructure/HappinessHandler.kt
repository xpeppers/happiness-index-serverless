package happiness.infrastructure

import daikon.lambda.HttpHandler
import daikon.lambda.LambdaCall
import happiness.AddHappinessVoteUseCase
import happiness.GetHappinessVotesUseCase
import happiness.RealClock

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
        post("/happiness", AddVoteAction(addVoteUseCase, RealClock()))
        get("/happiness/votes", GetVotesAction(getVotesUseCase))
    }
}


