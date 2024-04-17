package com.diploma.panchev.apigraphql.service.impl;

import com.diploma.panchev.apigraphql.auth.CustomJwtAuthenticationToken;
import com.diploma.panchev.apigraphql.exception.NotLoggedException;
import com.diploma.panchev.apigraphql.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof CustomJwtAuthenticationToken token) {
            return token.getName();
        }
        throw new NotLoggedException();
    }
}
