package happiness.infrastructure

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test


class VotesOnS3E2ETest {

    @BeforeEach
    internal fun setUp() {
        clearS3Bucket()
    }

    @Disabled("WIP: e2e template")
    @Test
    internal fun `http calls should append the body message to s3 bucket`() {
        post(URL, """{"message":"1"}""")
        val firstBucketContent = getBucketContent()

        assertThat(firstBucketContent)
            .isEqualTo("1")

        post(URL, """{"message":"2"}""")
        val secondBucketContent = getBucketContent()

        assertThat(secondBucketContent)
            .isEqualTo("1\n2")
    }

    private fun getBucketContent(): String {
        TODO("Not yet implemented")
    }

    private fun post(url: String, body: String) {
        TODO("Not yet implemented")
    }

    private fun clearS3Bucket() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val URL = ""
    }

}