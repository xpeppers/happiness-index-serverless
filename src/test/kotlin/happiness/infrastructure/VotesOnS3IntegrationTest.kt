package happiness.infrastructure

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest


class VotesOnS3IntegrationTest {

    private lateinit var s3: S3Client

    @BeforeEach
    fun setUp() {
        s3 = S3Client.builder().credentialsProvider(DefaultCredentialsProvider.create()).build()
        createBucketIfNotExists(BUCKET_NAME)
        deleteBucketObject(BUCKET_NAME, FILE_NAME)
    }

    @Test
    @Disabled
    fun name() {
        VotesOnS3().add("aVote")

        val votesBucket = readVotesFromBucket("happiness-index-dev", FILE_NAME)
        assertThat(votesBucket).contains("aVote")
    }

    @Test
    fun spike() {
        putObjectWithBody(BUCKET_NAME, FILE_NAME, "1\n2")

        val votes = readVotesFromBucket(BUCKET_NAME, FILE_NAME)

        assertThat(votes).containsExactly("1", "2")
    }

    private fun readVotesFromBucket(bucketName: String, fileName: String): List<String> {
        val responseInputStream =
            s3.getObject(GetObjectRequest.builder().bucket(bucketName).key(fileName).build())
        return responseInputStream.reader().readLines()
    }

    private fun createBucketIfNotExists(bucketName: String) {
        if (s3.listBuckets().buckets().any { it.name() == bucketName }) return
        s3.createBucket(CreateBucketRequest.builder().bucket(bucketName).build())
    }

    private fun deleteBucketObject(bucketName: String, fileName: String) {
        s3.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(fileName).build())
    }

    private fun putObjectWithBody(bucketName: String, fileName: String, body: String) {
        val putRequest = PutObjectRequest.builder().bucket(bucketName).key(fileName).build()
        s3.putObject(putRequest, RequestBody.fromString(body))
    }

    companion object {
        private const val BUCKET_NAME = "my-spike-bucket-xpeppers-test"
        private const val FILE_NAME = "votes"
    }
}