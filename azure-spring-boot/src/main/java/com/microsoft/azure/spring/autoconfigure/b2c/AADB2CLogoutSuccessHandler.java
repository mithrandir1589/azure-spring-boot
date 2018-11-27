/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.spring.autoconfigure.b2c;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AADB2CLogoutSuccessHandler implements LogoutSuccessHandler {

    private final AADB2CProperties b2cProperties;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public AADB2CLogoutSuccessHandler(@NonNull AADB2CProperties b2cProperties) {
        this.b2cProperties = b2cProperties;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        final String logoutSuccessURL = b2cProperties.getLogoutSuccessUrl();
        final String redirectURL = AADB2CURL.getOpenIdLogoutURL(b2cProperties, logoutSuccessURL, request);

        log.debug("Redirect to AAD B2C URL {} for expiring token.", redirectURL);
        log.debug("Expire AAD B2C token successful, will redirect to URL {}.", logoutSuccessURL);

        redirectStrategy.sendRedirect(request, response, redirectURL);
    }
}
