package com.coinsensor.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.coinsensor.common.config.LeaderElectionManager;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LeaderOnlyAspect {

	private final LeaderElectionManager leaderElectionManager;

	@Around("@annotation(com.coinsensor.common.annotation.LeaderOnly)")
	public Object checkLeadership(ProceedingJoinPoint joinPoint) throws Throwable {
		if (!leaderElectionManager.tryAcquireLeadership()) {
			return null;
		}
		return joinPoint.proceed();
	}
}
