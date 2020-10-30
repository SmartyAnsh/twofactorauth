# twofactorauth
Two Factor Auth App build with Spring Boot, Spring Security, and Websockets

How it works?
A user logs in using a username and password in the application. Once authenticated, the app will send an SMS/Email (not implemented yet) to the user that contains a link to verify his login.
Meanwhile, he will be holding up on a page and can't go forward in the application.
Once the user clicks on the link sent in SMS/Email, the login page refreshes itself and the user lands on the home page.
