package happiness.uat

import happiness.BASE_URL
import happiness.infrastructure.BUCKET_NAME
import happiness.infrastructure.KEY_NAME
import io.restassured.RestAssured.post
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddHappinessVoteAcceptanceTest {

    private val s3 by lazy { S3Client.create() }

    @BeforeEach
    fun setUp() {
        s3.createBucketIfNotExists(BUCKET_NAME)
        s3.emptyBucketKey(
            BUCKET_NAME,
            KEY_NAME
        )
    }

    @Test
    fun `http calls should append the vote to s3 bucket`() {
        post("$BASE_URL/happiness/1")
            .then()
            .statusCode(201)

        assertThat(votes()).containsExactly("1")

        post("$BASE_URL/happiness/2")
            .then()
            .statusCode(201)

        assertThat(votes()).containsExactly("1", "2")
    }

    private fun votes() = s3.readFromBucket(BUCKET_NAME, KEY_NAME)

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

    private fun S3Client.emptyBucketKey(bucketName: String, bucketKey: String) {
        writeToBucket(bucketName, bucketKey, "")
    }
}