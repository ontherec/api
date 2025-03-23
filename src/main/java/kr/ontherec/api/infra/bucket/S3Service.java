package kr.ontherec.api.infra.bucket;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class S3Service {
    static private final Duration EXPIRE_DURATION = Duration.ofMinutes(10);

    @Value("${spring.cloud.aws.s3.bucket}")
    private String BUCKET_NAME;

    private final S3Presigner presigner;

    public String createUploadUrl(FileExtension ext, String username) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(generateKeyName(ext.getFolder(), username, ext.getValue()))
                .contentType(ext.getContentType())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(EXPIRE_DURATION)
                .putObjectRequest(objectRequest)
                .build();

        return presigner.presignPutObject(presignRequest).url().toExternalForm();
    }

    private String generateKeyName(String folder, String username, String ext) {
        return folder + "/" + username + "/" + UUID.randomUUID() + "." + ext;
    }
}
