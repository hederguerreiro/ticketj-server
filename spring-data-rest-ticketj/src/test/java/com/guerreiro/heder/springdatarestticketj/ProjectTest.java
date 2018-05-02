package com.guerreiro.heder.springdatarestticketj;

import static org.hamcrest.CoreMatchers.containsString;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.guerreiro.heder.springdatarestticketj.repositories.ProjectRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProjectTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProjectRepository projectRepository;

	@Before
	public void deleteAllProjectsBeforeTests() throws Exception {
		projectRepository.deleteAll();
	}

	@Test
	public void shouldReturnProjectRepositoryIndex() throws Exception {
		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$._links.projects").exists());
	}

	@Test
	public void shouldCreateProjectEntity() throws Exception {
		mockMvc.perform(post("/projects")
				.content("{\"name\": \"Project Test\"," + "\"description\":\"Test project to this application.\" }"))
				.andExpect(status().isCreated()).andExpect(header().string("Location", containsString("projects/")));
	}

	@Test
	public void shouldRetrieveProjectEntity() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/projects").content(
						"{\"name\": \"Project Test\"," + "\"description\":\"Test project to this application.\" }"))
				.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Project Test"))
				.andExpect(jsonPath("$.description").value("Test project to this application."));
	}

	@Test
	public void shouldUpdateProjectEntity() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/projects").content(
						"{\"name\": \"Project Test\"," + "\"description\":\"Test project to this application.\" }"))
				.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(put(location)
				.content("{\"name\": \"Project Test 2\"," + "\"description\":\"Test project to this application.\" }"))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Project Test 2"))
				.andExpect(jsonPath("$.description").value("Test project to this application."));
	}

	@Test
	public void shouldPartiallyProjectUpdateEntity() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/projects").content(
						"{\"name\": \"Project Test\"," + "\"description\":\"Test project to this application.\" }"))
				.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(patch(location).content("{\"name\": \"Project Test 3\"}")).andExpect(status().isNoContent());
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Project Test 3"))
				.andExpect(jsonPath("$.description").value("Test project to this application."));
	}

	@Test
	public void shouldDeleteProjectEntity() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/projects").content(
						"{\"name\": \"Project Test\"," + "\"description\":\"Test project to this application.\" }"))
				.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().isNoContent());
		mockMvc.perform(get(location)).andExpect(status().isNotFound());
	}

}
