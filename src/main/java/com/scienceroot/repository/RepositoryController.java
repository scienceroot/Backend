package com.scienceroot.repository;

import com.fasterxml.jackson.annotation.JsonView;
import com.scienceroot.security.ActionForbiddenException;
import static com.scienceroot.security.SecurityConstants.SECRET;
import static com.scienceroot.security.SecurityConstants.TOKEN_PREFIX;
import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserService;
import com.scienceroot.user.ResourceNotFoundException;
import com.scienceroot.user.UserNotFoundException;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private final RepositoryService repositoryService;
    private final ApplicationUserService userService;

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
    @JsonView(RepositoryViews.Authorized.class)
    public Repository create(
            @RequestHeader(name = "Authorization") String jwt,
            @RequestBody Repository repository
    ) { 
        Optional<ApplicationUser> creator = this.userFromJwt(jwt);
        
        if (!creator.isPresent()) {
            throw new UserNotFoundException();
        } 
        
        repository.setCreator(creator.get());
        repository = this.repositoryService.create(repository);
        
        return repository;
    }
    
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @JsonView(RepositoryViews.Public.class)
    public Repository get(
            @PathVariable(name = "id") UUID repositoryId
    ) {
        Optional<Repository> repo = this.repositoryService.findOne(repositoryId);
        
        if(!repo.isPresent()) {
            throw new ResourceNotFoundException();
        } 
        
        return repo.get();
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/{id}", method = RequestMethod.POST)
    public String storeText(
            @PathVariable(name = "id") UUID repositoryId,
            @RequestHeader(name = "Authorization") String jwt,
            @RequestBody DataRequestBody dataRequest
    ) {
        Repository repository = this.get(repositoryId);
        Optional<ApplicationUser> creator = this.userFromJwt(jwt);
        
        if(!creator.isPresent()) {
            throw new UserNotFoundException();
        }
        
        if(!repository.getCreator().getId().toString().equals(creator.get().getId().toString())) {
            throw new ActionForbiddenException();
        }
        
        
        try {
            String tx;
            tx = this.repositoryService.store(repository, dataRequest);
            
            return tx;
        } catch (IOException ex) {
            Logger.getLogger(RepositoryController.class.getName()).log(Level.SEVERE, null, ex);
            
            return null;
        }
        
        
    }
    
    private Optional<ApplicationUser> userFromJwt(String jwt) {
        String userMail = Jwts.parser().setSigningKey(SECRET.getBytes())
                .parseClaimsJws(jwt.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();
        
        return this.userService.findByMail(userMail);
    }
}
