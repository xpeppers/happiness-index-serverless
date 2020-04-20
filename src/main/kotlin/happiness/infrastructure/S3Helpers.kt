package happiness.infrastructure

import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

internal fun S3Client.readFromBucket(bucketName: String, keyName: String): List<String> {
    val getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(keyName)
        .build()

    return getObject(getObjectRequest).reader().readLines()
}

internal fun S3Client.writeToBucket(bucketName: String, keyName: String, content: String) {
    val putRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(keyName)
        .build()

    putObject(putRequest, RequestBody.fromString(content))
}