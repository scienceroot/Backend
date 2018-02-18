package com.scienceroot.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class ApplicationUserController {
	
	public ApplicationUserRepository userRepository;

	public ApplicationUserController(@Autowired ApplicationUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity usersID(@PathVariable("id") long id) throws JsonParseException, JsonProcessingException {

		ApplicationUser user = this.userRepository.findOne(id);

		if (user != null) {
			String UserStr = new ObjectMapper().writeValueAsString(user);
			return new ResponseEntity(UserStr, HttpStatus.OK);
		} else {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity usersIDedit(@PathVariable("id") long id, @RequestBody ApplicationUser user) {
		ApplicationUser userToUpdate = this.userRepository.findOne(id);

		if (userToUpdate != null) {
			this.userRepository.save(user);

			return new ResponseEntity(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * @RequestMapping(value = "/interests", method = RequestMethod.GET) public
	 *                       ResponseEntity searchInterests(@RequestParam("q")
	 *                       String q) throws IOException{ Session session =
	 *                       getSessionFactory().openSession();
	 *                       session.beginTransaction(); System.out.println(q);
	 *                       Query query = session.createQuery("from Interest where
	 *                       name like :name"); query.setParameter("name",
	 *                       '%'+q+'%'); List interests = query.list(); StringWriter
	 *                       sw = new StringWriter(); ObjectMapper mapper = new
	 *                       ObjectMapper(); mapper.writeValue(sw, interests);
	 *                       return new ResponseEntity(sw.toString(),
	 *                       HttpStatus.CREATED);
	 * 
	 *                       }
	 **/
}
