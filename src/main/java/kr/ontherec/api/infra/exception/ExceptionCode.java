package kr.ontherec.api.infra.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = Shape.OBJECT)
@JsonIgnoreProperties("status")
public interface ExceptionCode {
    String getCode();
    HttpStatus getStatus();
    String getMessage();
}