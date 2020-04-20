package happiness

val BASE_URL: String by lazy {
    System.getenv("HAPPINESS_INDEX_BASE_URL") ?: "https://g49lpxwuhd.execute-api.eu-west-1.amazonaws.com/dev"
}

