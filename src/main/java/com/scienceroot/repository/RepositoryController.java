package com.scienceroot.repository;

import static com.scienceroot.security.SecurityConstants.SECRET;
import static com.scienceroot.security.SecurityConstants.TOKEN_PREFIX;
import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserService;
import com.scienceroot.user.ResourceNotFoundException;
import com.scienceroot.user.UserNotFoundException;
import io.jsonwebtoken.Jwts;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
@RequestMapping("/repositories")
public class RepositoryController {
    
    private RepositoryService repositoryService;
    private ApplicationUserService userService;

    @Autowired
    public RepositoryController(
            RepositoryService repositoryService,
            ApplicationUserService userService
    ) {
        this.repositoryService = repositoryService;
        this.userService = userService;
    }
    

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public Repository create(
            @RequestHeader(name = "Authorization") String jwt,
            @RequestBody Repository repository
    ) {
        String userMail = Jwts.parser().setSigningKey(SECRET.getBytes())
                .parseClaimsJws(jwt.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();
        
        Optional<ApplicationUser> creator = this.userService.findByMail(userMail);
        
        if (!creator.isPresent()) {
            throw new UserNotFoundException();
        } else {
            repository.setCreator(creator.get());
        }
        
        return this.repositoryService.save(repository);
    }
    
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Repository get(
            @PathVariable(name = "id") UUID repositoryId
    ) {
        Optional<Repository> repo = this.repositoryService.findOne(repositoryId);
        
        if(!repo.isPresent()) {
            throw new ResourceNotFoundException();
        } 
        
        return repo.get();
    }
}
