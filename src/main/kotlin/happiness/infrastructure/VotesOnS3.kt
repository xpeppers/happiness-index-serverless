package happiness.infrastructure

import happiness.addvote.UserVote
import happiness.addvote.Votes
import happiness.getvotes.Vote
import software.amazon.awssdk.services.s3.S3Client
import java.time.LocalDateTime
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

class VotesOnS3(private val bucketName: String, private val keyName: String) : Votes {

    private val s3 by lazy { S3Client.create() }

    override fun add(vote: UserVote) {
        val existingVotes = s3.readFromBucket(bucketName, keyName)
        val newVotes = existingVotes.plus("${vote.date.format(ISO_DATE_TIME)};${vote.vote}")

        s3.writeToBucket(bucketName, keyName, newVotes.joinToString("\n"))
    }

    override fun all(): List<Vote> = s3
        .readFromBucket(bucketName, keyName)
        .map { Vote(it.toInt()) }

    override fun all2(): List<UserVote> = s3
        .readFromBucket(bucketName, keyName)
        .map {
            val (date, vote) = it.split(";")
            UserVote(vote = vote.toInt(), date = parse(date), userId = "", location = "")
        }

}