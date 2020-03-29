package happiness

import daikon.lambda.HttpHandler

class HappinessHandler : HttpHandler() {
    override fun routing() {
        post("/happiness/:vote") { _, res -> res.status(201); res.write("Thanks for voting :D") }
    }
}