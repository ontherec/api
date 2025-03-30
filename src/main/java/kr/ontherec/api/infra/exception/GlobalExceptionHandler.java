package kr.ontherec.api.infra.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionCode> handleCustomException(CustomException ex) {
        log.error("🚨 CustomException occurred: {} 🚨\n{}", ex.getMessage(), getStackTraceAsString(ex));

        return ResponseEntity.status(ex.getExceptionCode().getStatus())
                .body(ex.getExceptionCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionCode> handleServerException(RuntimeException ex) {
        log.error("🚨 InternalException occurred: {} 🚨\n{}", ex.getMessage(), getStackTraceAsString(ex));

        return ResponseEntity.status(CommonExceptionCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(CommonExceptionCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionCode> handleNotFoundException() {
        return ResponseEntity.status(CommonExceptionCode.NOT_FOUND.getStatus())
                .body(CommonExceptionCode.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationError(MethodArgumentNotValidException ex) {

        return ResponseEntity.status(CommonExceptionCode.NOT_VALID.getStatus())
                .body(Map.of("code", CommonExceptionCode.NOT_VALID.getCode(), "message",
                        Objects.requireNonNull(ex.getAllErrors().get(0).getDefaultMessage())));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ExceptionCode> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        return ResponseEntity.status(CommonExceptionCode.NOT_VALID.getStatus())
                .body(CommonExceptionCode.NOT_VALID);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionCode> handleHttpMessageNotReadableException() {
        return ResponseEntity.status(CommonExceptionCode.NOT_VALID.getStatus())
                .body(CommonExceptionCode.NOT_VALID);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionCode> handleMethodArgumentTypeMismatchException() {
        return ResponseEntity.status(CommonExceptionCode.TYPE_MISMATCH.getStatus())
                .body(CommonExceptionCode.TYPE_MISMATCH);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionCode> handleMissingServletRequestParameterException() {
        return ResponseEntity.status(CommonExceptionCode.MISSING_PARAMETER.getStatus())
                .body(CommonExceptionCode.MISSING_PARAMETER);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ExceptionCode> handleHttpMediaTypeNotSupportedException() {
        return ResponseEntity.status(CommonExceptionCode.UNSUPPORTED_MEDIA_TYPE.getStatus())
                .body(CommonExceptionCode.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionCode> handleAuthenticationException() {
        return ResponseEntity.status(CommonExceptionCode.UNAUTHORIZED.getStatus())
                .body(CommonExceptionCode.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionCode> handleAccessDeniedException() {
        return ResponseEntity.status(CommonExceptionCode.FORBIDDEN.getStatus())
                .body(CommonExceptionCode.FORBIDDEN);
    }

    private String getStackTraceAsString(RuntimeException ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}
