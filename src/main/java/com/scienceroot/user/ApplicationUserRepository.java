package com.scienceroot.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, UUID> {

    Optional<ApplicationUser> findByMail(String mail);
}
