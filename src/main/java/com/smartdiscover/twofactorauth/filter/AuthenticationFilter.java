package com.smartdiscover.twofactorauth.filter;

import com.smartdiscover.twofactorauth.domain.TwoFactorAuth;
import com.smartdiscover.twofactorauth.repository.TwoFactorAuthRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@Order(1)
public class AuthenticationFilter implements Filter {

    private final static Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private TwoFactorAuthRepo twoFactorAuthRepo;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        TwoFactorAuth twoFactorAuth = (TwoFactorAuth) session.getAttribute("authToken");

        String path = req.getRequestURI();

        //confirm once if twoFactorAuth is not verified
        if (twoFactorAuth != null && !twoFactorAuth.getVerified()) {
            twoFactorAuth = twoFactorAuthRepo.findTwoFactorAuthByUsernameAndAuthToken(twoFactorAuth.getUsername(), twoFactorAuth.getAuthToken());
        }

        if ("/home".equals(path) && (twoFactorAuth == null || !twoFactorAuth.getVerified())) {
            LOG.info("Request {} not authenticated", req.getRequestURL());
            return;
        }

        //TODO handle redirects - /home should be the landing page all times
        //for the initial authentication - it should be /twoFactorAuth

        if (twoFactorAuth != null && twoFactorAuth.getVerified()) {
            session.setAttribute("authToken", twoFactorAuth);
        }

        chain.doFilter(request, response);
    }
    // other methods
}
