package com.scienceroot.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, UUID> {

    Optional<ApplicationUser> findByMail(String mail);

    @Query("SELECT user " +
            "FROM ApplicationUser user " +
            "WHERE lower(user.forename) like lower(concat('%', :query, '%')) or lower(user.lastname) like lower(concat('%', :query, '%')) ")
    List<ApplicationUser> search(@Param("query") String query);
}
