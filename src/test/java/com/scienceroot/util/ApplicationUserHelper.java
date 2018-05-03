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
    
    public static ApplicationUser getTestUser(String suffix) {
        ApplicationUser testUser = new ApplicationUser();
        testUser.setLastname("Test" + suffix);
        testUser.setForename("Test" + suffix);
        testUser.setMail("test"  + suffix + "@test.de");
        
        return testUser;
    }
}
