package pl.noxhours.filters;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class ForceLogoutFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (((HttpServletRequest) servletRequest).getSession().getAttribute("forceLogout") != null && (Boolean) ((HttpServletRequest) servletRequest).getSession().getAttribute("forceLogout") && !((HttpServletRequest) servletRequest).getRequestURI().contains("admin/show/") && !((HttpServletRequest) servletRequest).getRequestURI().contains("settings/show/") && !((HttpServletRequest) servletRequest).getRequestURI().matches(".*\\..{2,4}")) {
            ((HttpServletResponse) servletResponse).sendRedirect("/logout");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
