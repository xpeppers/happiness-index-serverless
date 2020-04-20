package happiness.infrastructure

import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest

internal fun S3Client.createBucketIfNotExists(bucketName: String) {
    if (!exists(bucketName))
        createBucket(CreateBucketRequest.builder().bucket(bucketName).build())
}

internal fun S3Client.deleteBucket(bucketName: String, key: String) {
    listBuckets().buckets().firstOrNull { it.name() == bucketName }?.let {
        deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build())
        deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build())
    }
}

internal fun S3Client.exists(bucketName: String) =
    listBuckets().buckets().any { it.name() == bucketName }

internal fun S3Client.emptyBucketKey(bucketName: String, bucketKey: String) {
    writeToBucket(bucketName, bucketKey, "")
}

internal fun S3Client.deleteKeyOnBucket(bucketName: String, keyName: String) =
    deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(keyName).build())
