package io.javabrains.springsecurityjpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringSecurityJpaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testHome() throws Exception {

		// Tests basic access to homepage is accessible by anyone and that the get request works
		String result = mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	public void testXSS() throws Exception {
		String result = mockMvc.
				perform(get("/admin/display_user?userID=<script>alert('XSS!')</script>").accept(MediaType.TEXT_HTML_VALUE))
				.andExpect(status().isNotAcceptable())
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	public void testSQLInjection() throws Exception {
		String result = mockMvc.
				perform(get("/admin/new_campaign?name=mytestcamp&type=food%27%29%3B+INSERT+INTO+campaigns+" +
						"%28%60name%60%2C+%60type_label%60%29+VALUES+%28%27thisisaninjection%27%2C+%27food")
						.accept(MediaType.TEXT_HTML_VALUE))
				.andExpect(status().isAccepted())
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andReturn().getResponse().getContentAsString();
	}
}

