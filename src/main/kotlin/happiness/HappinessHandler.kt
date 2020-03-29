package happiness

import daikon.lambda.HttpHandler

class HappinessHandler : HttpHandler() {
    override fun routing() {
        get("/") { _, res -> res.write("Hello world") }
    }
}