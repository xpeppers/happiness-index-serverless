package happiness.infrastructure

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.time.LocalDate

fun main() {
    val region = Region.EU_WEST_1
    val credentialsProvider = DefaultCredentialsProvider.create()
    val s3 = S3Client.builder()
        .credentialsProvider(credentialsProvider)
        .region(region)
        .build()

//    val bucketRequest = CreateBucketRequest.builder().bucket("my-spike-bucket-xpeppers-test").build()
//    s3.createBucket(bucketRequest)

    val bucketName = "my-spike-bucket-xpeppers-test"
    val putRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key("votes-${LocalDate.now()}")
        .build()
    s3.putObject(putRequest, RequestBody.fromString("1"))

    s3.getObject(GetObjectRequest.builder().bucket(bucketName).key("votes-${LocalDate.now()}").build())


    val listBucketsResponse = s3.listBuckets()
    println(listBucketsResponse.buckets())

//    s3.deleteBucket(DeleteBucketRequest.builder().bucket("my-spike-bucket-xpeppers-test").build())
}