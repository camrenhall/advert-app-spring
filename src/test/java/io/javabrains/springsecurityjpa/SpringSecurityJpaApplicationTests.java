package io.javabrains.springsecurityjpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringSecurityJpaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	// http://localhost:8080/admin/display_user?userID=%3Cscript%3Ealert(%27XSS%20Success!%27)%3C/script%3E
	@Test
	public void testXSSInjection() throws Exception {
			String result = mockMvc.
					perform(get("/admin/display_user?userID=<script>alert('XSS!')</script>").accept(MediaType.TEXT_HTML_VALUE))
					.andExpect(content().contentType("text/html;charset=UTF-8"))
					.andReturn().getResponse().getContentAsString();
			Assert.isTrue(result.equals("<h1>Illegal arguments</h1>"));
	}

	//http://localhost:8080/admin/new_campaign?name=mytestcamp&type=food%27%29%3B+INSERT+INTO+campaigns+%28%60name%60%2C+%60type_label%60%29+VALUES+%28%27thisisaninjection%27%2C+%27food
	@Test
	public void testSQLInjection() throws Exception {
		String result = mockMvc.
				perform(get("/admin/new_campaign?name=mytestcamp&type=food%27%29%3B+INSERT+INTO+campaigns+" +
						"%28%60name%60%2C+%60type_label%60%29+VALUES+%28%27thisisaninjection%27%2C+%27food")
						.accept(MediaType.TEXT_HTML_VALUE))
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andReturn().getResponse().getContentAsString();
		Assert.isTrue(result.equals("<h1>Illegal arguments</h1>"));
	}

	@Test
	public void test_Admin_Display_User() throws Exception {
		String result = mockMvc.
				perform(get("/admin/display_user?userID=DROP+campaigns+FROM+springsecurity")
						.accept(MediaType.TEXT_HTML_VALUE))
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andReturn().getResponse().getContentAsString();
		Assert.isTrue(result.equals("<h1>Illegal arguments</h1>"));
	}

	@Test
	public void test_Admin_Add_Campaign() throws Exception {
		String result = mockMvc.
				perform(get("/admin/add_campaign?userID=nan&projectID=lol")
						.accept(MediaType.TEXT_HTML_VALUE))
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andReturn().getResponse().getContentAsString();
		Assert.isTrue(result.equals("<h1>Illegal arguments</h1>"));
	}

	@Test
	public void test_Admin_Remove_Campaign() throws Exception {
		String result = mockMvc.
				perform(get("/admin/remove_campaign?userID='''&projectID=%27")
						.accept(MediaType.TEXT_HTML_VALUE))
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andReturn().getResponse().getContentAsString();
		Assert.isTrue(result.equals("<h1>Illegal arguments</h1>"));
	}

	@Test
	public void test_Admin_Complete_Campaign() throws Exception {
		String result = mockMvc.
				perform(get("/admin/complete_campaign?userID=20947two3&projectID=34")
						.accept(MediaType.TEXT_HTML_VALUE))
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andReturn().getResponse().getContentAsString();
		Assert.isTrue(result.equals("<h1>Illegal arguments</h1>"));
	}

	@Test
	public void test_Admin_New_Campaign() throws Exception {
		String result = mockMvc.
				perform(get("/admin/new_campaign?name=2&type=food")
						.accept(MediaType.TEXT_HTML_VALUE))
				.andExpect(content().contentType("text/html;charset=UTF-8"))
				.andReturn().getResponse().getContentAsString();
		Assert.isTrue(result.equals("<h1>Illegal arguments</h1>"));
	}
}

