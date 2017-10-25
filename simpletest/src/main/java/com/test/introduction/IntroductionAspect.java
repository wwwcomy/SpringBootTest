package com.test.introduction;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class IntroductionAspect {

	@DeclareParents(value = "com.test.introduction.*ServiceImpl", defaultImpl = DefaultUsageTracked.class)
	public static UsageTracked mixin;

	@Before("execution(* com.test.introduction..*ServiceImpl.*(..)) && this(usageTracked)")
	public void recordUsage(UsageTracked usageTracked) {
		usageTracked.incrementUseCount();
	}

}
