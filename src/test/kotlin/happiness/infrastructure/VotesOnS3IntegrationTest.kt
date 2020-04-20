import happiness.infrastructure.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import software.amazon.awssdk.services.s3.S3Client
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VotesOnS3IntegrationTest {

    private val s3 by lazy { S3Client.create() }

    @BeforeEach
    fun setUp() {
        s3.createBucketIfNotExists(BUCKET_NAME)
        s3.emptyBucketKey(BUCKET_NAME, KEY_NAME)
    }

    @AfterEach
    fun tearDown() {
        s3.deleteBucket(BUCKET_NAME, KEY_NAME)
    }

    @Test
    fun `append votes to s3`() {
        val votesOnS3 = VotesOnS3(BUCKET_NAME, KEY_NAME)
        votesOnS3.add("aVote")
        votesOnS3.add("anotherVote")

        val votes = s3.readFromBucket(BUCKET_NAME, KEY_NAME)
        assertThat(votes).contains("aVote", "anotherVote")
    }

    companion object {
        private val BUCKET_NAME = "happiness-index-test-${UUID.randomUUID()}"
        private const val KEY_NAME = "votes"
    }
}