package com.nhnacademy.bookstoreback.global.aspect;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.global.exception.InavailableAuthorizationException;
import com.nhnacademy.bookstoreback.global.exception.UnauthorizedException;
import com.nhnacademy.bookstoreback.global.exception.UserNotActiveException;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleAuthorizationAspect {
	private final UserRepository userRepository;

	@Before("@annotation(authorizeRole)")
	public void checkUserRole(JoinPoint joinPoint, AuthorizeRole authorizeRole) throws Throwable {
		CurrentUserDetails currentUserDetails = getCurrentUserDetails();

		if (currentUserDetails == null) {
			throw new UnauthorizedException();
		}

		Long userId = currentUserDetails.getUserId();
		User user = userRepository.findById(userId).orElseThrow();
		if ("DORMANT".equals(user.getStatus().getUserStatusName())) {
			throw new UserNotActiveException();
		}

		List<String> roles = Arrays.asList(authorizeRole.value());
		boolean hasRole = currentUserDetails.getAuthorities().stream()
			.anyMatch(grantedAuthority -> roles.contains(grantedAuthority.getAuthority()));

		if (!hasRole) {
			throw new InavailableAuthorizationException();
		}
	}

	private CurrentUserDetails getCurrentUserDetails() {
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication != null && authentication.getPrincipal() instanceof CurrentUserDetails) {
				return (CurrentUserDetails)authentication.getPrincipal();
			}
		}
		return null;
	}
}
