package com.scienceroot.repository;

import com.fasterxml.jackson.annotation.JsonView;
import com.scienceroot.blockchain.Blockchain;
import com.scienceroot.repository.exceptions.DataTransactionSizeException;
import com.scienceroot.security.ActionForbiddenException;
import static com.scienceroot.security.SecurityConstants.SECRET;
import static com.scienceroot.security.SecurityConstants.TOKEN_PREFIX;
import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserService;
import com.scienceroot.user.ResourceNotFoundException;
import com.scienceroot.user.UserNotFoundException;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
@RequestMapping("/repositories")
public class RepositoryController {

    private final RepositoryService repositoryService;
    private final ApplicationUserService userService;
    private final Blockchain blockchain;

    @Autowired
    public RepositoryController(
            RepositoryService repositoryService,
            ApplicationUserService userService,
            Blockchain blockchain
    ) {
        this.repositoryService = repositoryService;
        this.userService = userService;
        this.blockchain = blockchain;
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

        this.blockchain.sendInitialFunds(repository.getPublicKey());
        
        return repository;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/", method = RequestMethod.GET)
    @JsonView(RepositoryViews.Public.class)
    public List<Repository> get(
            @RequestHeader(name = "Authorization") String jwt,
            @RequestParam(name = "userId") UUID userId
    ) { 
        ApplicationUser filterUser = this.userService.findOne(userId);
        
        if (filterUser == null) {
            throw new UserNotFoundException();
        }
        
        return this.repositoryService.find(filterUser);
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
    public String storePage(
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
            String txId = this.repositoryService.store(repository, dataRequest);
            
            return txId;
        } catch (IOException ex) {
            Logger.getLogger(RepositoryController.class.getName()).log(Level.SEVERE, null, ex);

            return null;
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public String updatePage(
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
            String txId = this.repositoryService.update(repository, dataRequest);
            
            return txId;
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
