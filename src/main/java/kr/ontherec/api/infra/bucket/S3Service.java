package kr.ontherec.api.infra.bucket;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;


@Service
@RequiredArgsConstructor
@Transactional
public class S3Service {
    static private final Duration EXPIRE_DURATION = Duration.ofMinutes(10);
    static private final String DELIMITER = "_";

    @Value("${spring.cloud.aws.s3.bucket}")
    private String BUCKET_NAME;

    private final S3Presigner presigner;

    public String createImagePresignedUrl(String username) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(generateKeyName("images/o/", username))
                .contentType("image/*")
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(EXPIRE_DURATION)
                .putObjectRequest(objectRequest)
                .build();

        return presigner.presignPutObject(presignRequest).url().toExternalForm();
    }

    public String createDocsPresignedUrl(String username) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(generateKeyName("docs/", username))
                .contentType("text/plain; charset=utf-8")
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(EXPIRE_DURATION)
                .putObjectRequest(objectRequest)
                .build();

        return presigner.presignPutObject(presignRequest).url().toExternalForm();
    }

    private String generateKeyName(String prefix, String username) {
        return prefix + username + DELIMITER + System.currentTimeMillis();
    }
}
