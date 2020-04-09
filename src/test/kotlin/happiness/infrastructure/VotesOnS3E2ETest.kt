package happiness.infrastructure

import io.restassured.RestAssured
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VotesOnS3E2ETest {

    private val s3 by lazy { S3Client.create() }

    @BeforeEach
    fun setUp() {
        s3.createBucketIfNotExists(BUCKET_NAME)
        s3.writeToBucket(BUCKET_NAME, KEY_NAME,"")
    }

    @AfterEach
    fun tearDown() {
        s3.deleteBucket(BUCKET_NAME, KEY_NAME)
    }

    @Disabled("WIP: e2e template")
    @Test
    fun `http calls should append the vote to s3 bucket`() {
        post("$BASE_URL/happiness/1")
        val firstBucketContent = s3.readFromBucket(BUCKET_NAME, KEY_NAME)

        assertThat(firstBucketContent)
            .containsExactly("1")

        post("$BASE_URL/happiness/2")
        val secondBucketContent = s3.readFromBucket(BUCKET_NAME, KEY_NAME)

        assertThat(secondBucketContent)
            .containsExactly("1","2")
    }

    private fun post(url: String) {
        RestAssured.post(url).then()
            .statusCode(201)
    }

    private fun S3Client.readFromBucket(bucketName: String, keyName: String): List<String> {
        val responseInputStream = getObject(GetObjectRequest.builder().bucket(bucketName).key(keyName).build())
        return responseInputStream.reader().readLines()
    }

    private fun S3Client.createBucketIfNotExists(bucketName: String) {
        if (!exists(bucketName))
            createBucket(CreateBucketRequest.builder().bucket(bucketName).build())
    }

    private fun S3Client.exists(bucketName: String) =
        listBuckets().buckets().any { it.name() == bucketName }

    private fun S3Client.writeToBucket(bucketName: String, keyName: String, body: String) {
        val putRequest = PutObjectRequest.builder().bucket(bucketName).key(keyName).build()
        putObject(putRequest, RequestBody.fromString(body))
    }

    private fun S3Client.deleteBucket(bucketName: String, key: String) {
        listBuckets().buckets().firstOrNull { it.name() == bucketName }?.let {
            deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build())
            deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build())
        }
    }

    companion object {
        private const val BASE_URL = "https://8y4rzbgztl.execute-api.eu-west-1.amazonaws.com/dev"
        private const val BUCKET_NAME = "happiness-index-tbd"
        private const val KEY_NAME = "votes"
    }

}