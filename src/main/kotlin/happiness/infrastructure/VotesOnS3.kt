package happiness.infrastructure

import happiness.addvote.Votes
import happiness.getvotes.Vote
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

class VotesOnS3(private val bucketName: String, private val keyName: String) : Votes {

    private val s3 by lazy { S3Client.create() }

    override fun add(vote: String) {
        val existingVotes = s3.readFromBucket(bucketName, keyName)
        val newVotes = existingVotes + vote
        s3.putObjectWithBody(bucketName, keyName, newVotes.joinToString("\n"))
    }

    override fun all(): List<Vote> = s3
        .readFromBucket(bucketName, keyName)
        .map { Vote(it.toInt()) }

    private fun S3Client.readFromBucket(bucketName: String, keyName: String): List<String> {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(keyName)
            .build()

        return getObject(getObjectRequest).reader().readLines()
    }

    private fun S3Client.putObjectWithBody(bucketName: String, keyName: String, body: String) {
        val putRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(keyName)
            .build()

        putObject(putRequest, RequestBody.fromString(body))
    }
}