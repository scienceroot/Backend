package com.scienceroot.util;

import com.scienceroot.user.ApplicationUser;

/**
 *
 * @author svenseemann
 */
public class ApplicationUserHelper {
    
    public static ApplicationUser getTestUser() {
        ApplicationUser testUser = new ApplicationUser();
        testUser.setLastname("Test");
        testUser.setForename("Test");
        testUser.setMail("test@test.de");
        
        return testUser;
    }
}
