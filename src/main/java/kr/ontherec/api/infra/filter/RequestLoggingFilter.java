package kr.ontherec.api.infra.filter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.tinylog.Logger;

public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

    @Override
    protected void beforeRequest(@NonNull HttpServletRequest request, @NonNull String message) {
        Logger.info(message);
    }

    @Override
    protected void afterRequest(@NonNull HttpServletRequest request, @NonNull String message) {
        Logger.info(message);
    }
}
