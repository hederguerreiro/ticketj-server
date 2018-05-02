package com.guerreiro.heder.springdatarestticketj;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
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

import com.guerreiro.heder.springdatarestticketj.entities.Ticket;
import com.guerreiro.heder.springdatarestticketj.repositories.TicketRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TicketTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TicketRepository ticketRepository;

	@Before
	public void deleteAllTicketsBeforeTests() throws Exception {
		ticketRepository.deleteAll();
	}

	@Test
	public void shouldTicketNumberHaveValidRepresentation() throws Exception {
		Ticket ticket = new Ticket();
		assertThat(ticket.getNumber(), containsString("#"));
	}

	@Test
	public void shouldReturnTicketRepositoryIndex() throws Exception {
		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$._links.tickets").exists());
	}

	@Test
	public void shouldCreateTicketEntity() throws Exception {
		mockMvc.perform(post("/tickets").content("{\"summary\": \"Issue in the system.\","
				+ "\"details\":\"Issue in the system.\",\"status\":\"OPENED\" }")).andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("tickets/")));
	}

	@Test
	public void shouldRetrieveTicketEntity() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/tickets").content("{\"summary\": \"Issue in the system.\","
						+ "\"details\":\"Issue in the system.\",\"status\":\"OPENED\" }"))
				.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk())
				.andExpect(jsonPath("$.summary").value("Issue in the system."))
				.andExpect(jsonPath("$.details").value("Issue in the system."));
	}

	@Test
	public void shouldUpdateTicketEntity() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/tickets").content("{\"summary\": \"Issue in the system.\","
						+ "\"details\":\"Issue in the system.\",\"status\":\"OPENED\" }"))
				.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(put(location).content("{\"summary\": \"Issue in the system CRITICAL**.\","
				+ "\"details\":\"Issue in the system. PLEASE!\",\"status\":\"OPENED\" }"))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location)).andExpect(status().isOk())
				.andExpect(jsonPath("$.summary").value("Issue in the system CRITICAL**."))
				.andExpect(jsonPath("$.details").value("Issue in the system. PLEASE!"));
	}

	@Test
	public void shouldPartiallyTicketUpdateEntity() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/tickets").content("{\"summary\": \"Issue in the system.\","
						+ "\"details\":\"Issue in the system.\",\"status\":\"OPENED\" }"))
				.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(patch(location).content("{\"summary\": \"Issue solved???\"}"))
				.andExpect(status().isNoContent());
		mockMvc.perform(get(location)).andExpect(status().isOk())
				.andExpect(jsonPath("$.summary").value("Issue solved???"))
				.andExpect(jsonPath("$.details").value("Issue in the system."));
	}

	@Test
	public void shouldDeleteTicketEntity() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/tickets").content("{\"summary\": \"Issue in the system.\","
						+ "\"details\":\"Issue in the system.\",\"status\":\"OPENED\" }"))
				.andExpect(status().isCreated()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().isNoContent());
		mockMvc.perform(get(location)).andExpect(status().isNotFound());
	}

}
