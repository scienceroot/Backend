package com.scienceroot.search;

import com.scienceroot.config.HibernateUtil;
import com.scienceroot.search.Arxiv;
import com.scienceroot.search.Paper;
import com.scienceroot.search.SearchResult;
import com.scienceroot.user.ApplicationUser;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author husche
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public SearchResult search(@RequestParam("q") String q){
        Arxiv arxiv = new Arxiv();
        String url = "http://export.arxiv.org/api/query?search_query=ti:" + q;
        Paper[] papers = arxiv.runSearch(url);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from scr_user where forename || lastname like ? or lastname || forename like ?");
        query.setParameter(0, '%'+q+'%');
        query.setParameter(1, '%'+q+'%');
        List usersList = query.list();
        ApplicationUser[] users = new ApplicationUser[usersList.size()];
        for (int i = 0; i < usersList.size(); i++){
            users[i] = (ApplicationUser)usersList.get(i);
        }
        return new SearchResult(papers, users);
    }
    
}