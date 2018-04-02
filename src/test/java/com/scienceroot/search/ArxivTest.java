/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scienceroot.search;

import org.junit.Before;
import org.junit.Test;

import static com.scienceroot.search.SearchService.URL_ARXIV;
import java.util.List;
import static org.junit.Assert.assertNotNull;

/**
 * @author husche
 */
public class ArxivTest {

	private Arxiv arxiv;

	@Before
	public void setUp() throws Exception {
		this.arxiv = new Arxiv();
	}

	@Test
	public void testRunSearch() {

		String query = "auto";
		String url = URL_ARXIV + "?search_query=ti:" + query;
		List<Paper> result = this.arxiv.runSearch(url);

		assertNotNull(result);
		for (Paper pape : result) {
			//assertNotNull(pape.getId());
			assertNotNull(pape.getTitle());
			assertNotNull(pape.getPublished());
			//assertNotNull(pape.getSummary());
			//assertNotNull(pape.getUpdated());
			assertNotNull(pape.getLink());
			assertNotNull(pape.getAuthor());
		}

	}
        

}
