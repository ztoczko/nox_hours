package pl.noxhours.configuration.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class NoxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        request.getSession().setAttribute("loggedUserName", ((NoxUserDetails) authentication.getPrincipal()).getFullName());
        request.getSession().setAttribute("loggedUserId", ((NoxUserDetails) authentication.getPrincipal()).getId());
        request.getSession().setAttribute("loggedUserAdminStatus", authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")));
        request.getSession().setAttribute("loggedUserSuperAdminStatus", authentication.getAuthorities().contains(new SimpleGrantedAuthority("SUPERADMIN")));
        if (request.getRequestURI().contains("/login")) {
            response.sendRedirect("/dashboard");
        } else {
            response.sendRedirect(request.getRequestURI());
        }
    }
}
