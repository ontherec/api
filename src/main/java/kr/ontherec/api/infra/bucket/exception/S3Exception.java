package kr.ontherec.api.infra.bucket.exception;

import kr.ontherec.api.global.exception.CustomException;
import lombok.Getter;

@Getter
public class S3Exception extends CustomException {
    public S3Exception(S3ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
