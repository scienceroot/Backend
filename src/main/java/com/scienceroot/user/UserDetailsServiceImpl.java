package com.scienceroot.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Collections.emptyList;

/**
 *
 * @author husche
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOG = Logger.getLogger(UserDetailsServiceImpl.class.getName());
    private final ApplicationUserRepository applicationUserRepository;

    /**
     *
     * @param applicationUserRepository
     */
    public UserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    /**
     *
     * @param mail
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

        Optional<ApplicationUser> applicationUser = applicationUserRepository.findByMail(mail);

        if (!applicationUser.isPresent()) {
            LOG.info("login for mail " + mail + " not found");
            throw new UsernameNotFoundException(mail);
        }

        LOG.info("login for mail " + mail + " found");
        return new User(applicationUser.get().getMail(), applicationUser.get().getPassword(), emptyList());
    }
}
