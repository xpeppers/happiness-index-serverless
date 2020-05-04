package happiness.infrastructure

import happiness.addvote.UserVote
import happiness.addvote.Votes
import software.amazon.awssdk.services.s3.S3Client
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter.ISO_DATE_TIME

class VotesOnS3(private val bucketName: String, private val keyName: String) : Votes {

    private val s3 by lazy { S3Client.create() }

    override fun add(vote: UserVote) {
        val existingVotes = s3.readFromBucket(bucketName, keyName)
        val newVotes = existingVotes.plus(vote.toRecord())

        s3.writeToBucket(bucketName, keyName, newVotes.joinToString("\n"))
    }

    override fun all(): List<UserVote> = s3
        .readFromBucket(bucketName, keyName)
        .map { voteRecord -> voteRecord.split(";") }
        .map { (date, vote, userId, location) ->
            UserVote(
                vote = vote.toInt(),
                date = parse(date),
                userId = userId,
                location = location
            )
        }
}

private fun UserVote.toRecord() = "${date.format(ISO_DATE_TIME)};${vote};${userId};${location}"