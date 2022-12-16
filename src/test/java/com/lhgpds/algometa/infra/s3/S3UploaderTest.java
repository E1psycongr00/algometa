package com.lhgpds.algometa.infra.s3;

import static org.assertj.core.api.Assertions.assertThat;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.lhgpds.algometa.internal.member.domain.vo.ImageLink;
import io.findify.s3mock.S3Mock;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.mock.web.MockMultipartFile;


class S3UploaderTest {

    private static final String BUCKET_NAME = "testbucket";
    private static final String REGION = "ap-northeast-2";


    @Test
    @DisplayName("S3 업로드 연결 및 upload 테스트")
    void s3uploadConnectionTest() throws IOException {
        // given
        S3Mock s3Mock = S3Config.s3Mock();
        AmazonS3Client amazonS3Client = (AmazonS3Client) S3Config.amazonS3(s3Mock);
        S3Uploader s3Uploader = new S3Uploader(amazonS3Client, BUCKET_NAME);

        String path = "test.png";
        String contentType = "image/png";
        String dirName = "test";

        MockMultipartFile file = new MockMultipartFile("test", path, contentType,
            "test".getBytes());

        // when
        ImageLink urlPath = s3Uploader.upload(file, dirName);

        // then
        assertThat(urlPath.toString()).contains(path);
        assertThat(urlPath.toString()).contains(dirName);

        s3Mock.stop();
    }


    @TestConfiguration
    static class S3Config {

        public static S3Mock s3Mock() {
            return new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
        }


        public static AmazonS3 amazonS3(S3Mock s3Mock) {
            s3Mock.start();
            AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:8001", REGION);
            AmazonS3 client = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();
            client.createBucket(BUCKET_NAME);

            return client;
        }
    }
}