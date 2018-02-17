/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scienceroot.user.ApplicationUser;
import com.scienceroot.user.ApplicationUserRepository;


/**
 *
 * @author husche
 */
@RestController
@RequestMapping("/users")
@CrossOrigin
public class RegistrationController {
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	public ApplicationUserRepository userRepository;
	
	public RegistrationController(
		@Autowired ApplicationUserRepository userRepository,
		@Autowired BCryptPasswordEncoder bCryptPasswordEncoder
	) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	
    @PostMapping(value = "/register")
    public ResponseEntity createUser(@RequestBody ApplicationUser user) throws JsonProcessingException {
        	System.out.println(user.getUsername());
        if(this.userRepository.findByUsername(user.getUsername()).isPresent()) {
        		return new ResponseEntity("User already exists", HttpStatus.BAD_REQUEST);
        } else {
        		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        		this.userRepository.save(user);
            
        		String userStr = new ObjectMapper().writeValueAsString(user);
            
            return new ResponseEntity(userStr, HttpStatus.CREATED);
        }
    }

    /**@RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody ApplicationUser user) throws JsonProcessingException {
    	
    		Optional<ApplicationUser> dbUser = this.userRepository.findByUsername(user.getUsername());
    		
    		if(dbUser.isPresent()) {
    			if(!BCrypt.checkpw(user.getPassword(), dbUser.get().getPassword())) {
    	            String userStr = new ObjectMapper().writeValueAsString(user);
    	            
    	            return new ResponseEntity(userStr, HttpStatus.CREATED);
    			} else {
    				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    			}
    		} else {
    			return new ResponseEntity(HttpStatus.NOT_FOUND);
    		}
    }**/

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
