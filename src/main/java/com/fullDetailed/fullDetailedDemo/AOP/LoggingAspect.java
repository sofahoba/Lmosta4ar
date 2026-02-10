package com.fullDetailed.fullDetailedDemo.AOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.aspectj.lang.JoinPoint;


@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.fullDetailed.fullDetailedDemo.services.impl..*.*(..))")
    public void serviceMethods() {}

    @Around("serviceMethods()")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("========================================");
        log.info("==== ENTERING====: {}.{}", className, methodName);
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                log.info("   Arg[{}]: {}", i, args[i]);
            }
        } else {
            log.info("   No arguments");
        }

        long start = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - start;

            log.info("==== COMPLETED=====: {}.{}", className, methodName);
            log.info("   Duration: {} ms", duration);
            log.info("   Result: {}", result);
            log.info("========================================");

            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;

            log.error("==== FAILED ====: {}.{}", className, methodName);
            log.error("   Duration: {} ms", duration);
            log.error("   Exception: {}", e.getMessage());
            log.info("========================================");

            throw e;
        }
    }
}