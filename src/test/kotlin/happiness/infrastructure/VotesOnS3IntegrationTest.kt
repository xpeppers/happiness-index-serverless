import happiness.infrastructure.VotesOnS3
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VotesOnS3IntegrationTest {

    private val s3 by lazy { S3Client.create() }

    @BeforeEach
    fun setUp() {
        s3.createBucketIfNotExists(BUCKET_NAME)
    }

    @AfterEach
    fun tearDown() {
        s3.deleteBucket(BUCKET_NAME, KEY_NAME)
    }

    @Test
    fun `reads votes from s3`() {
        s3.writeToBucket(BUCKET_NAME, KEY_NAME, "")
        val votesOnS3 = VotesOnS3(BUCKET_NAME, KEY_NAME)
        votesOnS3.add("aVote")
        votesOnS3.add("anotherVote")

        val votesBucket = s3.readFromBucket(BUCKET_NAME, KEY_NAME)

        assertThat(votesBucket).contains("aVote", "anotherVote")
    }

    @Test
    fun `can read from s3 bucket`() {
        s3.writeToBucket(BUCKET_NAME, KEY_NAME, "1\n2")
        val votes = s3.readFromBucket(BUCKET_NAME, KEY_NAME)

        assertThat(votes).containsExactly("1", "2")
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

    private fun S3Client.deleteKeyOnBucket(bucketName: String, keyName: String) =
        deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(keyName).build())

    companion object {
        private val BUCKET_NAME = "happiness-index-test-${UUID.randomUUID()}"
        private const val KEY_NAME = "votes"
    }
}