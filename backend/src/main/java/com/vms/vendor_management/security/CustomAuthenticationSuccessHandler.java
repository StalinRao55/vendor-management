package com.vms.vendor_management.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = "";
        String redirectUrl = "/";

        for (GrantedAuthority authority : authorities) {
            role = authority.getAuthority();
            if (role.equals("ROLE_ADMIN")) {
                redirectUrl = "/admin-dashboard.html";
                break;
            } else if (role.equals("ROLE_VENDOR")) {
                redirectUrl = "/vendor-dashboard.html";
                break;
            } else if (role.equals("ROLE_PROCUREMENT_MANAGER") || role.equals("ROLE_FINANCE_TEAM")) {
                redirectUrl = "/employee-dashboard.html";
                break;
            }
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format("{\"success\": true, \"role\": \"%s\", \"redirectUrl\": \"%s\"}", role, redirectUrl));
        response.getWriter().flush();
    }
}
