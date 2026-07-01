package com.manage_and_book_badminton.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.manage_and_book_badminton.service.BookingService.createBooking(..))")
    public void bookingServiceCreatePointcut() {
        // Pointcut for createBooking
    }

    @AfterReturning(pointcut = "bookingServiceCreatePointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("[AUDIT - SUCCESS] Method {} executed successfully with result: {}", joinPoint.getSignature().getName(), result);
        // Note: For a real world scenario, the result object (e.g. Booking object) can be inspected to log "Khách hàng A đặt thành công Sân số 2..."
    }

    @AfterThrowing(pointcut = "bookingServiceCreatePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("[AUDIT - FAILED] Method {} failed with exception: {}", joinPoint.getSignature().getName(), e.getMessage());
        // Detailed error log
    }
}
