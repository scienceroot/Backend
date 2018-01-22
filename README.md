# Backend
Basics of the backend based on java with spring

Requires h2database http://www.h2database.com/html/main.html running as a service

checking of jwt token not yet enabled by default for the ease of debugging, can be activated by changing 'Application.java' line ~21ish:

registrationBean.addUrlPatterns("/PathToSecure/*");

Backend is using springboot, running 'Application.java' should suffice, will start committing .jars/.wars as well 
