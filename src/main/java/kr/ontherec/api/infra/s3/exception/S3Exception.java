package kr.ontherec.api.infra.s3.exception;

import kr.ontherec.api.infra.exception.CustomException;
import lombok.Getter;

@Getter
public class S3Exception extends CustomException {
    public S3Exception(S3ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
