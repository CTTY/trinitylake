/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trinitylake.storage.s3;

import io.trinitylake.StorageOpsTests;
import io.trinitylake.storage.CommonStorageOpsProperties;
import io.trinitylake.storage.LiteralURI;
import io.trinitylake.storage.StorageOps;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

@Testcontainers
public class TestAmazonS3StorageOps extends StorageOpsTests {
  @Container private static final MinIOContainer MINIO = MinioTestUtil.createContainer();

  private String tempBucket;
  private AmazonS3StorageOps storageOps;
  private static S3AsyncClient s3Client;

  @BeforeAll
  public static void setupClient() {
    s3Client = MinioTestUtil.createS3Client(MINIO);
  }

  @BeforeEach
  public void beforeEach() {
    tempBucket = String.valueOf(UUID.randomUUID());
    s3Client.createBucket(CreateBucketRequest.builder().bucket(tempBucket).build()).join();
    AmazonS3StorageOpsProperties s3StorageOpsProperties =
        new AmazonS3StorageOpsProperties(MinioTestUtil.createS3PropertiesMap(MINIO));
    storageOps =
        new AmazonS3StorageOps(CommonStorageOpsProperties.instance(), s3StorageOpsProperties);
  }

  @Override
  protected StorageOps storageOps() {
    return storageOps;
  }

  @Override
  protected LiteralURI createFileUri(String fileName) {
    String filePath = String.format("s3://%s/%s", tempBucket, fileName);
    return new LiteralURI(filePath);
  }

  @Override
  protected LiteralURI createDirectoryUri(String dirName) {
    String path = String.format("s3://%s/%s", tempBucket, dirName);
    return new LiteralURI(path);
  }
}
