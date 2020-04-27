package happiness.infrastructure

import happiness.addvote.UserVote
import happiness.addvote.Votes
import happiness.getvotes.Vote
import software.amazon.awssdk.services.s3.S3Client

class VotesOnS3(private val bucketName: String, private val keyName: String) : Votes {

    private val s3 by lazy { S3Client.create() }

    override fun add(vote: UserVote) {
        val existingVotes = s3.readFromBucket(bucketName, keyName)
        val newVotes = existingVotes + vote.vote.toString() // <== TODO possiamo semplicemente togliere il toString?

        s3.writeToBucket(bucketName, keyName, newVotes.joinToString("\n"))
    }

    override fun all(): List<Vote> = s3
        .readFromBucket(bucketName, keyName)
        .map { Vote(it.toInt()) }

}