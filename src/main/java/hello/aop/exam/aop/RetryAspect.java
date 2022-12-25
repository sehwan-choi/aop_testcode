package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {

        Exception exceptionHolder = null;

        for (int i = 0 ; i < retry.value() ; i++ ) {
            try {
                return joinPoint.proceed();
            } catch (Exception e) {
                log.info("[retry]{} try count={}/{}, args={}", joinPoint.getSignature(), i + 1 , retry.value(), joinPoint.getArgs());
                exceptionHolder = e;
            }
        }
        throw exceptionHolder;
    }
}
