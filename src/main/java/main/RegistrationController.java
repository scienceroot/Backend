/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

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
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author husche
 */
@RestController
public class RegistrationController {

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {

        String securePassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        ApplicationUser myUser = new ApplicationUser(username, securePassword);

        try (Session session = getSessionFactory().openSession()) {

            Query query = session.createQuery("from ApplicationUser where username = :username");
            query.setParameter("username", username);
            if (null == query.uniqueResult()) {
                //no duplicate usernames, save user to db
                session.beginTransaction();
                session.save(myUser);
                session.getTransaction().commit();

                JSONObject job = new JSONObject();
                job.put("uid", myUser.getId());
                return new ResponseEntity(job.toString(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        try (Session session = getSessionFactory().openSession()) {
            Query query = session.createQuery("from ApplicationUser where username = :username");
            query.setParameter("username", username);
            if (null == query.uniqueResult()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            ApplicationUser user = (ApplicationUser) query.uniqueResult();
            if (!BCrypt.checkpw(password, user.getPassword())) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }

            JWT jwt = new JWT();
            String jwtStr = jwt.createJWT("0", "ScienceRoot", username, 10000);
            JSONObject job = new JSONObject();
            job.put("uid", user.getId());
            job.put("authorization", jwtStr);
            return new ResponseEntity(job.toString(), HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity usersID(@PathVariable("id") long id) {
        try (Session session = getSessionFactory().openSession()) {
            Query query = session.createQuery("from ApplicationUser where id = :id");
            query.setParameter("id", id);
            if (null == query.uniqueResult()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
            ApplicationUser user = (ApplicationUser) query.uniqueResult();
            JSONObject job = new JSONObject();
            job.put("uid", user.getId());
            job.put("username", user.getUsername());
            return new ResponseEntity(job.toString(), HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity usersIDedit(@PathVariable("id") long id,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        try (Session session = getSessionFactory().openSession()) {
            Query query = session.createQuery("from ApplicationUser where id = :id");
            query.setParameter("id", id);
            if (null == query.uniqueResult()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

            String securePassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
            ApplicationUser user = (ApplicationUser) query.uniqueResult();
            user.setPassword(securePassword);
            user.setUsername(username);
            
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();

            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
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
