/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.mindrot.jbcrypt.BCrypt;
import main.security.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author husche
 */
@RestController
@CrossOrigin
public class RegistrationController {
	
	public ApplicationUserRepository userRepository;
	
	public RegistrationController(@Autowired ApplicationUserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody ApplicationUser user) throws JsonProcessingException {

        String securePassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        
        if(this.userRepository.findByUsername(user.getUsername()).isPresent()) {
        		return new ResponseEntity("User already exists", HttpStatus.BAD_REQUEST);
        } else {
        		this.userRepository.save(user);
        	
        		String token = new JWT().createJWT("0", "ScienceRoot", user.getUsername(), 10000);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("authorization", token);
            String userStr = new ObjectMapper().writeValueAsString(user);
            
            return new ResponseEntity(userStr, responseHeaders, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody ApplicationUser user) throws JsonProcessingException {
    	
    		Optional<ApplicationUser> dbUser = this.userRepository.findByUsername(user.getUsername());
    		
    		if(dbUser.isPresent()) {
    			if(!BCrypt.checkpw(user.getPassword(), dbUser.get().getPassword())) {
    				String token = new JWT().createJWT("0", "ScienceRoot", dbUser.get().getUsername(), 10000);
    	            HttpHeaders responseHeaders = new HttpHeaders();
    	            responseHeaders.set("authorization", token);
    	            String userStr = new ObjectMapper().writeValueAsString(user);
    	            
    	            return new ResponseEntity(userStr, responseHeaders, HttpStatus.CREATED);
    			} else {
    				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    			}
    		} else {
    			return new ResponseEntity(HttpStatus.NOT_FOUND);
    		}
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity usersID(@PathVariable("id") long id) throws JsonParseException, JsonProcessingException {
    	
    		ApplicationUser user = this.userRepository.findOne(id);
    		
    		if(user != null) {
    			String UserStr = new ObjectMapper().writeValueAsString(user);
            return new ResponseEntity(UserStr, HttpStatus.OK);
    		} else {
    			return new ResponseEntity(HttpStatus.NOT_FOUND);
    		}
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity usersIDedit(@PathVariable("id") long id, @RequestBody ApplicationUser user) {
    		ApplicationUser userToUpdate = this.userRepository.findOne(id);
    		
    		if(userToUpdate != null) {
    			this.userRepository.save(user);

    			return new ResponseEntity(HttpStatus.NO_CONTENT);
    		} else {
    			return new ResponseEntity(HttpStatus.NOT_FOUND);
    		}
    }
    
    /**@RequestMapping(value = "/interests", method = RequestMethod.GET)
    public ResponseEntity searchInterests(@RequestParam("q") String q) throws IOException{
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        System.out.println(q);
        Query query = session.createQuery("from Interest where name like :name");
        query.setParameter("name", '%'+q+'%');
        List interests = query.list();
        StringWriter sw = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(sw, interests);
        return new ResponseEntity(sw.toString(), HttpStatus.CREATED);       
        
    }**/

    @RequestMapping(value = "/hue")
    public String hue() {
        this.userRepository.findAll().forEach(user -> System.out.println(user.getId() + " " + user.getUsername() + " " + user.getPassword()));
        
       
        return "hue";
    }

}
