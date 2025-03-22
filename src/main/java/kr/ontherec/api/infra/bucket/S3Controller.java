package kr.ontherec.api.infra.bucket;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/images")
    ResponseEntity<String> createImagePresignedUrl(Authentication authentication) {
        String presignedUrl = s3Service.createImagePresignedUrl(authentication.getName());
        return ResponseEntity.ok().body(presignedUrl);
    }

    @PostMapping("/docs")
    ResponseEntity<String> createDocsPresignedUrl(Authentication authentication) {
        String presignedUrl = s3Service.createDocsPresignedUrl( authentication.getName());
        return ResponseEntity.ok().body(presignedUrl);
    }
}
