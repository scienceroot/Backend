package com.scienceroot.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:test-database.properties")
public class ApplicationUserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private ApplicationUserService service;

	@Autowired
	private ApplicationUserRepository repository;

	private ApplicationUser currentUser;

	@Before
	public void setUp() throws Exception {
		this.currentUser = new ApplicationUser();
		this.currentUser.setLastname("Test");
		this.currentUser.setForename("Test");
		this.currentUser = this.service.save(this.currentUser);
	}

	@After
	public void tearDown() throws Exception {
		this.repository.deleteAll();
	}

	@Test
	public void updateUser() throws Exception {

		// just to be sure, you can validate the start settings, defined in setUp()
		assertThat(this.currentUser, notNullValue());
		assertThat(this.currentUser.getLastname(), is("Test"));

		this.mockMvc

				// define your request url (PUT of '/users/{uuid}'), content, ...
				.perform(put("/users/" + this.currentUser.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content("{	" +
								"	\"lastname\":\"Test-Lastname\"," +
								"	\"forename\":\"Test-Forename\"" +
								"}"))

				// debug, prints a shit of info (remove this line, when not needed)
				.andDo(print())

				// validate the response
				.andExpect(status().isNoContent())
				.andExpect(jsonPath("$.lastname").value("Test-Lastname"))
				.andExpect(jsonPath("$.forename").value("Test-Forename"));

		// of course you can validate the state in the backend too
		this.currentUser = this.repository.findOne(this.currentUser.getId());
		assertThat(this.currentUser, notNullValue());
		assertThat(this.currentUser.getLastname(), is("Test-Lastname"));
	}
}