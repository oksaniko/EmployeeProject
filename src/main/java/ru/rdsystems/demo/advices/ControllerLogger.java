package ru.rdsystems.demo.advices;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerLogger {

	@Around("within(@org.springframework.web.bind.annotation.RestController *)")
	public Object callController(ProceedingJoinPoint joinPoint) throws Throwable{
		log.info(joinPoint.getSignature().toString());
		try{
			var result = joinPoint.proceed();
			log.info(result.toString());
			return result;
		} catch (Throwable e){
			log.error(e.getMessage(), e);
			throw e;
		}
	}

}
