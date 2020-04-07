import happiness.infrastructure.VotesOnS3
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VotesOnS3IntegrationTest {

    private val s3 by lazy { S3Client.create() }

    @BeforeEach
    fun setUp() {
        s3.createBucketIfNotExists(BUCKET_NAME)
        s3.putObjectWithBody(BUCKET_NAME, FILE_NAME, "1\n2")
    }

    @AfterEach
    fun tearDown() {
        s3.deleteBucketKey(BUCKET_NAME, FILE_NAME)
    }

    @Test
    @Disabled
    fun `reads votes from s3`() {
        VotesOnS3().add("aVote")
        val votesBucket = s3.readFromBucket(BUCKET_NAME, FILE_NAME)

        assertThat(votesBucket).contains("aVote")
    }

    @Test
    fun `can read from s3 bucket`() {
        val votes = s3.readFromBucket(BUCKET_NAME, FILE_NAME)

        assertThat(votes).containsExactly("1", "2")
    }

    private fun S3Client.readFromBucket(bucketName: String, keyName: String): List<String> {
        val responseInputStream =
            getObject(GetObjectRequest.builder().bucket(bucketName).key(keyName).build())
        return responseInputStream.reader().readLines()
    }

    private fun S3Client.createBucketIfNotExists(bucketName: String) {
        if (listBuckets().buckets().any { it.name() == bucketName }) return
        createBucket(CreateBucketRequest.builder().bucket(bucketName).build())
    }

    private fun S3Client.deleteBucketKey(bucketName: String, keyName: String) =
        deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(keyName).build())

    private fun S3Client.putObjectWithBody(bucketName: String, keyName: String, body: String) {
        val putRequest = PutObjectRequest.builder().bucket(bucketName).key(keyName).build()
        putObject(putRequest, RequestBody.fromString(body))
    }

    companion object {
        private const val BUCKET_NAME = "my-spike-bucket-xpeppers-test"
        private const val FILE_NAME = "votes"
    }
}