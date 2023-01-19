package kz.nearbygems.elastic.controllers;

import kz.nearbygems.elastic.contollers.PersonController;
import kz.nearbygems.elastic.models.web.Persons;
import kz.nearbygems.elastic.services.PersonService;
import kz.nearbygems.elastic.util.Json;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PersonService service;

  @Test
  public void search() throws Exception {

    final var search = "search";
    final var offset = 0;
    final var limit  = 1;

    final var record = new Persons.Record();
    record.id        = "id";
    record.email     = "email";
    record.firstName = "firstname";
    record.lastName  = "lastname";
    record.username  = "username";

    final var persons = new Persons();
    persons.records   = List.of(record);
    persons.totalHits = 1;

    doReturn(persons).when(service).search(eq(search), eq(offset), eq(limit));

    final var request = MockMvcRequestBuilders.get("/persons/search")
                                        .param("search", search)
                                        .param("offset", String.valueOf(offset))
                                        .param("limit", String.valueOf(limit));

    //
    final var mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    //

    verify(service, times(1)).search(eq(search), eq(offset), eq(limit));

    final var responseBody = mvcResult.getResponse().getContentAsString();

    assertEquals(Json.MAPPER.writeValueAsString(persons), responseBody, JSONCompareMode.STRICT);
  }

  @Test
  public void loadById() throws Exception {

    final var record = new Persons.Record();
    record.id        = "id";
    record.email     = "email";
    record.firstName = "firstname";
    record.lastName  = "lastname";
    record.username  = "username";

    doReturn(record).when(service).loadById(eq(record.id));

    final var request = MockMvcRequestBuilders.get("/persons/{id}", record.id);

    //
    final var mvcResult = mockMvc.perform(request)
                           .andExpect(status().isOk())
                           .andReturn();
    //

    verify(service, times(1)).loadById(eq(record.id));

    final var responseBody = mvcResult.getResponse().getContentAsString();

    assertEquals(Json.MAPPER.writeValueAsString(record), responseBody, JSONCompareMode.STRICT);
  }

}
