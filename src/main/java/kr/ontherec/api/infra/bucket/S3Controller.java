package kr.ontherec.api.infra.bucket;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/upload")
    ResponseEntity<String> createUploadUrl(Authentication authentication, @RequestParam String ext) {
        String url = s3Service.createUploadUrl(FileExtension.from(ext.toLowerCase()), authentication.getName());
        return ResponseEntity.ok().body(url);
    }
}
