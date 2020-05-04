import happiness.addvote.UserVote
import happiness.infrastructure.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import software.amazon.awssdk.services.s3.S3Client
import java.time.LocalDateTime.parse
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
    fun `append votes to s3 with date information`() {
        val votesOnS3 = VotesOnS3(BUCKET_NAME, KEY_NAME)
        votesOnS3.add(UserVote(vote = 1, userId = "1234", location = "MI", date = parse("2020-01-01T12:00:00")))
        votesOnS3.add(UserVote(vote = 4, userId = "1234", location = "MI", date = parse("2011-03-02T00:00:00")))

        val votes = s3.readFromBucket(BUCKET_NAME, KEY_NAME)
        assertThat(votes).contains("2020-01-01T12:00:00;1;1234;MI")
        assertThat(votes).contains("2011-03-02T00:00:00;4;1234;MI")
    }

    @Test
    fun `get all votes on s3 with data information`() {
        val votesOnS3 = VotesOnS3(BUCKET_NAME, KEY_NAME)
        votesOnS3.add(UserVote(vote = 1, userId = "1234", location = "MI", date = parse("2020-01-01T12:00:00")))
        votesOnS3.add(UserVote(vote = 4, userId = "1234", location = "MI", date = parse("2011-03-02T00:00:00")))

        val votes = votesOnS3.all()

        assertThat(votes).containsExactly(
            UserVote(vote = 1, date = parse("2020-01-01T12:00:00"), userId = "1234", location = "MI"),
            UserVote(vote = 4, date = parse("2011-03-02T00:00:00"), userId = "1234", location = "MI")
        )
    }

    companion object {
        private val BUCKET_NAME = "happiness-index-test-${UUID.randomUUID()}"
        private const val KEY_NAME = "votes"
    }
}