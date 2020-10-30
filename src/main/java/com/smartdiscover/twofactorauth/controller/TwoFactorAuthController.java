package com.smartdiscover.twofactorauth.controller;

import com.smartdiscover.twofactorauth.domain.TwoFactorAuth;
import com.smartdiscover.twofactorauth.model.LoginGreeting;
import com.smartdiscover.twofactorauth.repository.TwoFactorAuthRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.sql.Date;
import java.util.concurrent.CompletableFuture;

@Controller
public class TwoFactorAuthController {

    Logger log = LoggerFactory.getLogger(TwoFactorAuthController.class);

    @Autowired
    private TwoFactorAuthRepo twoFactorAuthRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/twoFactorAuth/authenticate")
    @ResponseBody
    public String authenticate(@RequestParam("user") String username, @RequestParam("token") String authToken) throws Exception {
        TwoFactorAuth twoFactorAuth = twoFactorAuthRepo.findTwoFactorAuthByUsernameAndAuthToken(username, authToken);

        //some conditional checks
        if (twoFactorAuth != null && twoFactorAuth.getVerified() == false && twoFactorAuth.getDateCreated().before(new Date(System.currentTimeMillis() - 900000L))) {

            //set twoFactorAuth request
            twoFactorAuth.setVerified(true);
            twoFactorAuth.setDateUpdated(new Date(System.currentTimeMillis()));
            twoFactorAuthRepo.save(twoFactorAuth);

            log.info("user " + twoFactorAuth.getUsername() + " is authenticated");

            //call async verifyLogin
            CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> sendMessage(twoFactorAuth));

            return "You are authenticated successfully!";
        } else {
            return "Your auth token is wrong or expired, please try login again";
        }
    }

    public boolean sendMessage(TwoFactorAuth twoFactorAuth) {
        simpMessagingTemplate.convertAndSendToUser(twoFactorAuth.getUsername(), "/queue/twoFactorAuth", new LoginGreeting("Hello, " + twoFactorAuth.getUsername()));
        return true;
    }

}
