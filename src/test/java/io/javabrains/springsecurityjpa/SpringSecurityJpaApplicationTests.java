package io.javabrains.springsecurityjpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringSecurityJpaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testHome() throws Exception {

		String result = mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		System.out.println("#####################################");
		System.out.println(result);
		Assert.isTrue(result.equals("<h1>Welcome</h1>"));
	}

	@Test
	public void testUser() throws Exception {

		String result = mockMvc.perform(post("/login?username=user&password=pass"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"))
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	public void testWrongUser() throws Exception {

		String result = mockMvc.perform(post("/login?username=user&password=pas"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login?error"))
				.andReturn().getResponse().getContentAsString();
	}

	@Test
	public void testXSS() throws Exception {
		String result = mockMvc.
				perform(get("/beautiful?username=user&password=pass&name=<script>alert('XSS!')</script>").accept(MediaType.TEXT_HTML_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andExpect(content().string(
						containsString("<p>Hello. You look beautiful! </p>Sincerely, <script>alert('XSS!')</script>")))
				.andReturn().getResponse().getContentAsString();
		Assert.isTrue(result.equals("<p>Hello. You look beautiful! </p>Sincerely, <script>alert('XSS!')</script>"));
		System.out.println(result);
	}

}





