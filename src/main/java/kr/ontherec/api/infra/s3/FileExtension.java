package kr.ontherec.api.infra.s3;

import kr.ontherec.api.infra.s3.exception.S3Exception;
import kr.ontherec.api.infra.s3.exception.S3ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public enum FileExtension {
    IMAGE_PNG("png", "images/o", "image/png"),
    IMAGE_JPG("jpg", "images/o", "image/jpeg"),
    IMAGE_JPEG("jpeg", "images/o", "image/jpeg"),
    IMAGE_WEBP("webp", "images/o", "image/webp"),
    IMAGE_AVIF("avif", "images/o", "image/avif"),

    DOCS_HWP("hwp", "docs", "application/octet-stream"),
    DOCS_HWPX("hwpx", "docs", "application/octet-stream"),
    DOCS_DOC("doc", "docs", "application/msword"),
    DOCS_DOCX("docx", "docs", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    private final String value;
    private final String folder;
    private final String contentType;

    public static FileExtension from(String value) {
        for (FileExtension ext : values()) {
            if (value.equals(ext.value)) {
                return ext;
            }
        }
        throw new S3Exception(S3ExceptionCode.NOT_VALID_FILE_EXTENSION);
    }
}
