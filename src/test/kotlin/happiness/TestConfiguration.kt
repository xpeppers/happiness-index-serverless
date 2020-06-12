package happiness

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader


val envVariables: Map<String, String> by lazy {
    val currentEnv: String = System.getenv("CURRENT_ENVIRONMENT")
    val reader = JsonReader(FileReader("./config/environment.json"))
    val json: Map<String, Map<String, String>> = Gson().fromJson(reader, Map::class.java)
    json[currentEnv] ?: throw Exception("test configuration error")
}


val TEST_BASE_URL: String by lazy {
    envVariables["baseUrl"] ?: throw Exception("test configuration error")
}
val TEST_BUCKET_NAME: String by lazy {
    envVariables["bucketName"] ?: throw Exception("test configuration error")
}
