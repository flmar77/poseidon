package com.poseidon.app.domain.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Map;

public abstract class UserHelper {

    public static String getUserName(Authentication authentication) {
        String userName;
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            Map<String, Object> attributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();
            userName = attributes.get("login").toString();
        } else {
            userName = authentication.getName();
        }
        return userName;
    }

}
