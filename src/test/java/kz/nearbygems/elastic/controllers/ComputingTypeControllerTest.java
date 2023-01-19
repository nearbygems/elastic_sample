package kz.nearbygems.elastic.controllers;

import kz.nearbygems.elastic.contollers.ComputingTypeController;
import kz.nearbygems.elastic.models.enums.DataType;
import kz.nearbygems.elastic.models.enums.SystemType;
import kz.nearbygems.elastic.models.web.ComputingTypeRecord;
import kz.nearbygems.elastic.services.ComputingTypeService;
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


@WebMvcTest(controllers = ComputingTypeController.class)
public class ComputingTypeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ComputingTypeService service;

  @Test
  public void load() throws Exception {

    final var system = SystemType.AIRFLOW;

    final var record = new ComputingTypeRecord();
    record.name        = "datamart";
    record.system      = system;
    record.type        = DataType.DATAMART;
    record.description = "some description";

    final var records = List.of(record);

    doReturn(records).when(service).load(eq(system));

    final var request = MockMvcRequestBuilders.get("/computing_types/load")
                                        .param("system", system.name());

    //
    final var mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    //

    verify(service, times(1)).load(eq(system));

    final var responseBody = mvcResult.getResponse().getContentAsString();

    assertEquals(Json.MAPPER.writeValueAsString(records), responseBody, JSONCompareMode.STRICT);
  }

  @Test
  public void loadById() throws Exception {

    final var record = new ComputingTypeRecord();
    record.name        = "datamart";
    record.system      = SystemType.AIRFLOW;
    record.type        = DataType.DATAMART;
    record.description = "some description";

    doReturn(record).when(service).loadById(eq(record.name));

    final var request = MockMvcRequestBuilders.get("/computing_types/{id}", record.name);

    //
    final var mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    //

    verify(service, times(1)).loadById(eq(record.name));

    final var responseBody = mvcResult.getResponse().getContentAsString();

    assertEquals(Json.MAPPER.writeValueAsString(record), responseBody, JSONCompareMode.STRICT);
  }

  @Test
  public void save() throws Exception {

    final var record = new ComputingTypeRecord();
    record.name        = "datamart";
    record.system      = SystemType.AIRFLOW;
    record.type        = DataType.DATAMART;
    record.description = "some description";

    doReturn(record.name).when(service).save(any(ComputingTypeRecord.class));

    final var request = MockMvcRequestBuilders.post("/computing_types/save")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(Json.MAPPER.writeValueAsString(record));

    //
    final var mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    //

    verify(service, times(1)).save(any(ComputingTypeRecord.class));

    final var response = mvcResult.getResponse().getContentAsString();

    assertThat(response).isNotNull();
    assertThat(response).isEqualTo(record.name);
  }

  @Test
  public void delete() throws Exception {

    final var record = new ComputingTypeRecord();
    record.id          = "id";
    record.name        = "datamart";
    record.system      = SystemType.AIRFLOW;
    record.type        = DataType.DATAMART;
    record.description = "some description";

    doNothing().when(service).delete(eq(record.id));

    final var request = MockMvcRequestBuilders.delete("/computing_types/{id}", record.id);

    //
    final var mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    //

    verify(service, times(1)).delete(eq(record.id));

    assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
  }

}
