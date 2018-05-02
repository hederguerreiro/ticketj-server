package com.guerreiro.heder.springdatarestticketj;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.guerreiro.heder.springdatarestticketj.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class UserTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Before
	public void deleteAllUsersBeforeTests() throws Exception {
		userRepository.deleteAll();
	}

	@Test
	public void shouldReturnUserRepositoryIndex() throws Exception {
		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$._links.users").exists()).andDo(document("users"));
	}

	@Test
	public void shouldCreateUserEntity() throws Exception {
		mockMvc.perform(post("/users").content(
				"{\"fullName\": \"John Smith\"," + "\"email\":\"johnsmith@test.com\",\"phone\":\"5544332211\" }"))
				.andExpect(status().isCreated()).andExpect(header().string("Location", containsString("users/")));
	}

	@Test
	public void shouldRetrieveUserEntity() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/users").content("{\"fullName\": \"John Smith\","
						+ "\"email\":\"johnsmith@test.com\",\"phone\":\"5544332211\" }"))
				.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(jsonPath("$.fullName").value("John Smith"))
				.andExpect(jsonPath("$.email").value("johnsmith@test.com"));
	}

	@Test
	public void shouldUpdateUserEntity() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/users").content("{\"fullName\": \"John Smith\","
						+ "\"email\":\"johnsmith@test.com\",\"phone\":\"5544332211\" }"))
				.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(put(location).content(
				"{\"fullName\": \"John Smith Jr.\"," + "\"email\":\"johnsmithjr@test.com\",\"phone\":\"5544332211\" }"))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location)).andExpect(status().isOk())
				.andExpect(jsonPath("$.fullName").value("John Smith Jr."))
				.andExpect(jsonPath("$.email").value("johnsmithjr@test.com"));
	}

	@Test
	public void shouldPartiallyUserUpdateEntity() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/users").content("{\"fullName\": \"John Smith\","
						+ "\"email\":\"johnsmith@test.com\",\"phone\":\"5544332211\" }"))
				.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(patch(location).content("{\"fullName\": \"John Smith Jr.\"}"))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location)).andExpect(status().isOk())
				.andExpect(jsonPath("$.fullName").value("John Smith Jr."))
				.andExpect(jsonPath("$.email").value("johnsmith@test.com"));
	}

	@Test
	public void shouldDeleteUserEntity() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/users").content("{\"fullName\": \"John Smith\","
						+ "\"email\":\"johnsmith@test.com\",\"phone\":\"5544332211\" }"))
				.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().isNoContent());
		mockMvc.perform(get(location)).andExpect(status().isNotFound());
	}

}
