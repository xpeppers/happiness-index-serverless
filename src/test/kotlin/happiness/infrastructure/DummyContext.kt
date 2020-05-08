package happiness.infrastructure

import daikon.core.Context

class DummyContext : Context {
    override fun addAttribute(key: String, value: Any) {}
    override fun <T> getAttribute(key: String): T {
        throw NotImplementedError()
    }
}
