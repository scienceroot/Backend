package com.scienceroot.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * @author husche
 */
public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, UUID> {

    /**
     *
     * @param mail
     * @return
     */
    Optional<ApplicationUser> findByMail(String mail);

    /**
     *
     * @param query
     * @return
     */
    @Query("SELECT user " +
            "FROM ApplicationUser user " +
            "WHERE lower(user.forename) like lower(concat('%', :query, '%')) or lower(user.lastname) like lower(concat('%', :query, '%')) "
            + "or lower(concat(' ', user.forename, ' ', user.lastname, ' ')) like lower(concat('%', :query, '%'))")
    List<ApplicationUser> search(@Param("query") String query);
}
