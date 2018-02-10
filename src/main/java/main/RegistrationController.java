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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static misc.HibernateUtil.getSessionFactory;
import org.hibernate.Session;
import org.hibernate.Query;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.mindrot.jbcrypt.BCrypt;
import main.security.JWT;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author husche
 */
@RestController
public class RegistrationController {

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody ApplicationUser user) throws JsonProcessingException {

        String securePassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));

        try (Session session = getSessionFactory().openSession()) {

            Query query = session.createQuery("from ApplicationUser where username = :username");
            query.setParameter("username", user.getUsername());
            if (null == query.uniqueResult()) {               
                //no duplicate usernames, save user to db
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();

                String token = new JWT().createJWT("0", "ScienceRoot", user.getUsername(), 10000);
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set("authorization", token);
                String userStr = new ObjectMapper().writeValueAsString(user);
                return new ResponseEntity(userStr, responseHeaders, HttpStatus.CREATED);
            } else {
                return new ResponseEntity("User already exists", HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody ApplicationUser user) throws JsonProcessingException {
        try (Session session = getSessionFactory().openSession()) {
            Query query = session.createQuery("from ApplicationUser where username = :username");
            query.setParameter("username", user.getUsername());
            if (null == query.uniqueResult()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            ApplicationUser dbuser = (ApplicationUser) query.uniqueResult();
            if (!BCrypt.checkpw(user.getPassword(), dbuser.getPassword())) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }

            String token = new JWT().createJWT("0", "ScienceRoot", dbuser.getUsername(), 10000);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("authorization", token);
            String userStr = new ObjectMapper().writeValueAsString(user);
            return new ResponseEntity(userStr, responseHeaders, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity usersID(@PathVariable("id") long id) throws JsonParseException, JsonProcessingException {
        try (Session session = getSessionFactory().openSession()) {
            Query query = session.createQuery("from ApplicationUser where id = :id");
            query.setParameter("id", id);
            if (null == query.uniqueResult()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            ApplicationUser user = (ApplicationUser) query.uniqueResult();
            String UserStr = new ObjectMapper().writeValueAsString(user);
            return new ResponseEntity(UserStr, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity usersIDedit(@PathVariable("id") long id,
            @RequestBody String user) {
        try (Session session = getSessionFactory().openSession()) {
            Query query = session.createQuery("from ApplicationUser where id = :id");
            query.setParameter("id", id);
            if (null == query.uniqueResult()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

            ApplicationUser existingUser = (ApplicationUser) query.uniqueResult();
            //user.setPassword(existingUser.getPassword());

            session.beginTransaction();
            session.save(existingUser);
            session.getTransaction().commit();

            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    @RequestMapping(value = "/interests", method = RequestMethod.GET)
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
        
    }

    @RequestMapping(value = "/hue")
    public String hue() {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        List res = session.createQuery("from ApplicationUser").list();
        for (ApplicationUser user : (List<ApplicationUser>) res) {
            System.out.println(user.getId() + " " + user.getUsername() + " " + user.getPassword());
        }
        session.getTransaction().commit();
        session.close();
        return "hue";
    }

}
