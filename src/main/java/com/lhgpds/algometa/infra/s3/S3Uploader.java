package com.lhgpds.algometa.infra.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.lhgpds.algometa.internal.member.domain.vo.ImageLink;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    private final String bucket;

    public ImageLink upload(MultipartFile multipartFile, String path) throws IOException {
        String s3FileName =
            path + "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getInputStream().available());
        amazonS3Client.putObject(bucket, s3FileName, multipartFile.getInputStream(),
            objectMetadata);
        String s3Url = amazonS3Client.getUrl(bucket, s3FileName).toString();
        return ImageLink.from(s3Url);
    }
}
