package kr.ontherec.api.infra.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.tinylog.Logger;

@Aspect
@Component
class StopWatchAdvice {
    @Around("@annotation(StopWatch)")
    public Object advice(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            Logger.tag("PERFORMANCE").info("spent time: {}ms", stopWatch.getTotalTimeMillis());
        }
    }
}