package kz.nearbygems.elastic.controllers;

import kz.nearbygems.elastic.contollers.StorageController;
import kz.nearbygems.elastic.models.web.StorageRecord;
import kz.nearbygems.elastic.services.StorageService;
import kz.nearbygems.elastic.util.Json;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = StorageController.class)
public class StorageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StorageService service;

  @Test
  public void load() throws Exception {

    final var record = new StorageRecord();
    record.id   = "id";
    record.name = "name";

    final var records = List.of(record);

    doReturn(records).when(service).load();

    final var request = MockMvcRequestBuilders.get("/storages/");

    //
    final var mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    //

    verify(service, times(1)).load();

    final var responseBody = mvcResult.getResponse().getContentAsString();

    assertEquals(Json.MAPPER.writeValueAsString(records), responseBody, JSONCompareMode.STRICT);
  }

  @Test
  public void loadById() throws Exception {

    final var record = new StorageRecord();
    record.id   = "id";
    record.name = "name";

    doReturn(record).when(service).loadById(eq(record.id));

    final var request = MockMvcRequestBuilders.get("/storages/{id}", record.id);

    //
    final var mvcResult = mockMvc.perform(request)
                           .andExpect(status().isOk())
                           .andReturn();
    //

    verify(service, times(1)).loadById(eq(record.id));

    final var responseBody = mvcResult.getResponse().getContentAsString();

    assertEquals(Json.MAPPER.writeValueAsString(record), responseBody, JSONCompareMode.STRICT);
  }

  @Test
  public void save() throws Exception {

    final var record = new StorageRecord();
    record.id   = "id";
    record.name = "name";

    doReturn(record.id).when(service).save(any(StorageRecord.class));

    final var request = MockMvcRequestBuilders.post("/storages/save")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(Json.MAPPER.writeValueAsString(record));

    //
    final var mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    //

    verify(service, times(1)).save(any(StorageRecord.class));

    final var response = mvcResult.getResponse().getContentAsString();

    assertThat(response).isNotNull();
    assertThat(response).isEqualTo(record.id);
  }

  @Test
  public void delete() throws Exception {

    final var record = new StorageRecord();
    record.id   = "id";
    record.name = "name";

    doNothing().when(service).delete(eq(record.id));

    final var request = MockMvcRequestBuilders.delete("/storages/{id}", record.id);

    //
    final var mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    //

    verify(service, times(1)).delete(eq(record.id));

    assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
  }

}
