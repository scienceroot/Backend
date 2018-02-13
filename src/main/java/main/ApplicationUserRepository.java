package main;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {
	
	public Optional<ApplicationUser> findByUsername(String username);

}
