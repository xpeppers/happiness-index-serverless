package happiness.infrastructure

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest

class VotesOnS3IntegrationTest {
    @Test
    @Disabled
    fun name() {
        VotesOnS3().add("aVote")

        val votesBucket = readVotesFromBucket("happiness-index-dev")
        assertThat(votesBucket).contains("aVote")
    }

    @Test
    fun spike() {
        println("start...")
        readVotesFromBucket("happiness-index-dev")
    }

    private fun readVotesFromBucket(bucketName: String): String {
        val credentialsProvider = DefaultCredentialsProvider.create()
        val s3 = S3Client.builder()
            .credentialsProvider(credentialsProvider)
            .build()
//        val bucket = s3.listBuckets().buckets().first { it.name() == bucketName }
        val getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key("votes").build()
        val responseInputStream = s3.getObject(getObjectRequest)


//        s3.deleteBucket(DeleteBucketRequest.builder().bucket("my-spike-bucket-xpeppers-test").build())
        return ""
    }
}