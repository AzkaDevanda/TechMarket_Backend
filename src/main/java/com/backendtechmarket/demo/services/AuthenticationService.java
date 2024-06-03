package com.backendtechmarket.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backendtechmarket.demo.config.MassageStrings;
import com.backendtechmarket.demo.entity.AuthenticationToken;
import com.backendtechmarket.demo.entity.User;
import com.backendtechmarket.demo.exeptions.AuthenticationFailException;
import com.backendtechmarket.demo.repository.TokenRepository;
import com.backendtechmarket.demo.utils.Helper;

@Service
public class AuthenticationService {
    @Autowired
    TokenRepository repository;

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        repository.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {
        return repository.findTokenByUser(user);
    }

    public User getUser(String token) {
        AuthenticationToken authenticationToken = repository.findTokenByToken(token);
        if (Helper.notNull(authenticationToken)) {
            if (Helper.notNull(authenticationToken.getUser())) {
                return authenticationToken.getUser();
            }
        }
        return null;
    }

    public void authenticate(String token) throws AuthenticationFailException {
        if (!Helper.notNull(token)) {
            throw new AuthenticationFailException(MassageStrings.AUTH_TOEKN_NOT_PRESENT);
        }
        if (!Helper.notNull(getUser(token))) {
            throw new AuthenticationFailException(MassageStrings.AUTH_TOEKN_NOT_VALID);
        }
    }

}
