package com.smartdiscover.twofactorauth;

import com.smartdiscover.twofactorauth.domain.TwoFactorAuth;
import com.smartdiscover.twofactorauth.repository.TwoFactorAuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.sql.Date;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private TwoFactorAuthRepo twoFactorAuthRepo;

    @Autowired
    private EmailServiceImpl emailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name = authentication.getName(); //user email
        String password = authentication.getCredentials().toString();
        Authentication auth = super.authenticate(authentication);

        if (isTwoFactorAuthEnabled()) {
            TwoFactorAuth twoFactorAuth = generateAuthenticationToken(name);

            ServletRequestAttributes attr = (ServletRequestAttributes)
                    RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("authToken", twoFactorAuth);

            String text = "http://localhost:8080/twoFactorAuth/authenticate?user="+twoFactorAuth.getUsername()+"&token="+twoFactorAuth.getAuthToken();
            emailService.sendSimpleMessage(name, "TwoFactorAuth Login Verification", text);

        }
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public boolean isTwoFactorAuthEnabled() {
        return true;
    }

    public TwoFactorAuth generateAuthenticationToken(String username) {
        //generate

        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setUsername(username);
        twoFactorAuth.setAuthToken(new BCryptPasswordEncoder().encode(username));
        twoFactorAuth.setDateCreated(new Date(System.currentTimeMillis()));
        twoFactorAuth.setVerified(false);
        twoFactorAuth = twoFactorAuthRepo.save(twoFactorAuth);

        return twoFactorAuth;
    }

}
