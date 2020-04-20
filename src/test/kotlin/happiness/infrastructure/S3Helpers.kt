package happiness.infrastructure

import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest

internal fun S3Client.readFromBucket(bucketName: String, keyName: String): List<String> {
    val responseInputStream = getObject(GetObjectRequest.builder().bucket(bucketName).key(keyName).build())
    return responseInputStream.reader().readLines()
}

internal fun S3Client.createBucketIfNotExists(bucketName: String) {
    if (!exists(bucketName))
        createBucket(CreateBucketRequest.builder().bucket(bucketName).build())
}

internal fun S3Client.exists(bucketName: String) =
    listBuckets().buckets().any { it.name() == bucketName }

internal fun S3Client.writeToBucket(bucketName: String, keyName: String, body: String) {
    val putRequest = PutObjectRequest.builder().bucket(bucketName).key(keyName).build()
    putObject(putRequest, RequestBody.fromString(body))
}

internal fun S3Client.emptyBucketKey(bucketName: String, bucketKey: String) {
    writeToBucket(bucketName, bucketKey, "")
}