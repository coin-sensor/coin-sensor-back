package com.coinsensor.common.aspect;

import static com.coinsensor.common.exception.ErrorCode.*;

import com.coinsensor.common.annotation.AuthorizeRole;
import com.coinsensor.common.exception.BusinessException;
import com.coinsensor.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminCheckAspect {
    
    private final UserRepository userRepository;
    
    @Before("@annotation(authorizeRole)")
    public void checkUserRole(JoinPoint joinPoint, AuthorizeRole authorizeRole) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String uuid = request.getHeader("uuid");
        
        if (uuid == null || !userRepository.findByUuid(uuid)
                .map(user -> authorizeRole.value().equals(user.getRole()))
                .orElse(false)) {
            throw new BusinessException(FORBIDDEN);
        }
    }
}