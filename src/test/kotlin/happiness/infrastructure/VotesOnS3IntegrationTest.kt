package happiness.infrastructure

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.core.sync.ResponseTransformer
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.nio.charset.Charset

class VotesOnS3IntegrationTest {

    @BeforeEach
    fun setUp() {
        deleteBucket("happiness-index-dev", "votes")
        createBucket("happiness-index-dev", "votes", "1")
    }

    @AfterEach
    fun tearDown() {
        deleteBucket("happiness-index-dev", "votes")
    }

    @Test
    @Disabled
    fun reads_votes_from_s3() {
        VotesOnS3().add("aVote")

        val votesBucket = readVotesFromBucket("happiness-index-dev", "votes")
        assertThat(votesBucket).contains("aVote")
    }

    @Test
    fun can_read_from_s3_bucket() {
        val votes = readVotesFromBucket("happiness-index-dev", "votes")
        assertThat(votes).isEqualTo("1")
    }

    private fun deleteBucket(bucketName: String, key: String) {
        val s3 = S3Client.create()
        s3.listBuckets().buckets().firstOrNull { it.name() == bucketName }?.let {
            s3.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build())
            s3.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build())
        }
    }

    private fun createBucket(bucketName: String, key: String, value: String) {
        val s3 = S3Client.create()

        val createBucketRequest = CreateBucketRequest.builder().bucket(bucketName).build()
        s3.createBucket(createBucketRequest)

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()
        s3.putObject(putObjectRequest, RequestBody.fromString(value))
    }

    private fun readVotesFromBucket(bucketName: String, key: String): String {
        val s3 = S3Client.create()

        val objectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()
        return s3.getObject(objectRequest, ResponseTransformer.toBytes()).asString(UTF_8)
    }

    companion object {
        private val UTF_8 = Charset.forName("UTF-8")
    }
}