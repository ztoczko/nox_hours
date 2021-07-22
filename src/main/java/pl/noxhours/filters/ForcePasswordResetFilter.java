package pl.noxhours.filters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pl.noxhours.user.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
@Order(2)
@RequiredArgsConstructor
public class ForcePasswordResetFilter implements Filter {

    private final UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (((HttpServletRequest) servletRequest).getSession().getAttribute("loggedUserId") != null && userService.read((Long) ((HttpServletRequest) servletRequest).getSession().getAttribute("loggedUserId")) != null && userService.read((Long) ((HttpServletRequest) servletRequest).getSession().getAttribute("loggedUserId")).getPasswordReset() && !((HttpServletRequest) servletRequest).getRequestURI().contains("/settings/changePassword") && !((HttpServletRequest) servletRequest).getRequestURI().contains("/login") && !((HttpServletRequest) servletRequest).getRequestURI().matches(".*\\..{2,4}")) {
            ((HttpServletResponse) servletResponse).sendRedirect("/settings/changePassword?forced=true");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
