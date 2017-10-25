package com.test.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.logger.Logger;

@Aspect
@Component
public class UserServiceAspect {

	private Logger logger;

	@Pointcut(value = "execution(* com.test.service..*.*(..))")
	private void target() {
		logger.log("Pointcut method...");
	}

	@Before("com.test.aspect.UserServiceAspect.target()")
	public void doBefore() {
		logger.log("In method doBefore");
	}

	@AfterReturning(pointcut = "com.test.aspect.UserServiceAspect.target()", returning = "retVal")
	public void doAfterReturning(Object retVal) {
		logger.log("In method doAfterReturning");
		logger.log("The return value is : " + retVal);
	}

	@After(value = "com.test.aspect.UserServiceAspect.target()")
	public void after(JoinPoint jp) {
		logger.log("In method after");
	}

	@AfterThrowing("com.test.aspect.UserServiceAspect.target()")
	public void doAfterThrow() {
		logger.log("In method doAfterThrow");
	}

	@Around(value = "com.test.aspect.UserServiceAspect.target()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		logger.log("In method doAround");
		Object object = pjp.proceed();// execution
		logger.log("Quitting doAround");
		return object;
	}

	@Around(value = "com.test.aspect.UserServiceAspect.target() && args(id)")
	public Object doAround2(ProceedingJoinPoint pjp, long id) throws Throwable {
		logger.log("In method doAround2, method should have params");
		logger.log("Param name = " + id);
		Object object = pjp.proceed();// execution
		logger.log("Quitting doAround2, method should have params");
		return object;
	}

	public Logger getLogger() {
		return logger;
	}

	@Autowired
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
