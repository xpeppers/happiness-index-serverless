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

    private val s3 by lazy {
        S3Client.create()
    }

    @BeforeEach
    fun setUp() {
        s3.deleteBucket("happiness-index-dev", "votes")
        s3.createBucket("happiness-index-dev", "votes", "1")
    }

    @AfterEach
    fun tearDown() {
        s3.deleteBucket("happiness-index-dev", "votes")
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

    private fun readVotesFromBucket(bucketName: String, key: String): String {
        val objectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()
        return s3.getObject(objectRequest, ResponseTransformer.toBytes()).asString(UTF_8)
    }

    private fun S3Client.deleteBucket(bucketName: String, key: String) {
        listBuckets().buckets().firstOrNull { it.name() == bucketName }?.let {
            deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build())
            deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build())
        }
    }

    private fun S3Client.createBucket(bucketName: String, key: String, value: String) {
        val createBucketRequest = CreateBucketRequest.builder().bucket(bucketName).build()
        createBucket(createBucketRequest)

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()
        putObject(putObjectRequest, RequestBody.fromString(value))
    }

    companion object {
        private val UTF_8 = Charset.forName("UTF-8")
    }
}