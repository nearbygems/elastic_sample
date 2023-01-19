package kz.nearbygems.elastic.services;

import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import kz.nearbygems.elastic.CatalogApplicationTests;
import kz.nearbygems.elastic.models.elastic.ComputingTypeDocument;
import kz.nearbygems.elastic.models.enums.DataType;
import kz.nearbygems.elastic.models.enums.SystemType;
import kz.nearbygems.elastic.models.web.ComputingTypeRecord;
import kz.nearbygems.elastic.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class ComputingTypeServiceImplTest extends CatalogApplicationTests {

  @Autowired
  private ComputingTypeService service;

  @Test
  public void load() {

    final var systemOk = SystemType.AIRFLOW;

    final var documentOk = new ComputingTypeDocument();
    documentOk.id          = "1";
    documentOk.name        = "datamart";
    documentOk.system      = systemOk;
    documentOk.type        = DataType.DATAMART;
    documentOk.description = "some description";

    save(documentOk);

    final var documentNo = new ComputingTypeDocument();
    documentNo.id          = "2";
    documentNo.name        = "trigger";
    documentNo.system      = SystemType.DMC;
    documentNo.type        = DataType.TRIGGER;
    documentNo.description = "some description";

    save(documentNo);

    //
    final var types = service.load(systemOk);
    //

    assertThat(types).isNotNull();
    assertThat(types).hasSize(1);

    final var type = types.get(0);

    assertThat(type.name).isEqualTo(documentOk.name);
    assertThat(type.system).isEqualTo(documentOk.system);
    assertThat(type.type).isEqualTo(documentOk.type);
    assertThat(type.description).isEqualTo(documentOk.description);
  }

  @Test
  public void loadById() {

    final var document = new ComputingTypeDocument();
    document.id          = "id";
    document.name        = "datamart";
    document.system      = SystemType.AIRFLOW;
    document.type        = DataType.DATAMART;
    document.description = "some description";
    document.isActual    = true;

    save(document);

    //
    final var record = service.loadById(document.id);
    //

    assertThat(record).isNotNull();
    assertThat(record.name).isEqualTo(document.name);
    assertThat(record.system).isEqualTo(document.system);
    assertThat(record.type).isEqualTo(document.type);
    assertThat(record.description).isEqualTo(document.description);
    assertThat(record.isActual).isTrue();
  }

  @Test
  public void save() {

    final var record = new ComputingTypeRecord();
    record.id          = "id";
    record.name        = "datamart";
    record.system      = SystemType.AIRFLOW;
    record.type        = DataType.DATAMART;
    record.description = "some description";
    record.isActual    = true;

    //
    service.save(record);
    //

    final var document = loadType(record.id);

    assertThat(document).isNotNull();
    assertThat(document.name).isEqualTo(record.name);
    assertThat(document.system).isEqualTo(record.system);
    assertThat(document.type).isEqualTo(record.type);
    assertThat(document.description).isEqualTo(record.description);
    assertThat(document.isActual).isTrue();
  }

  @Test
  public void delete() throws IOException {

    final var document = new ComputingTypeDocument();
    document.name        = "datamart";
    document.system      = SystemType.AIRFLOW;
    document.type        = DataType.DATAMART;
    document.description = "some description";

    save(document);

    //
    service.delete(document.name);
    //

    final var exists = client.exists(ex -> ex.index(Constants.COMPUTING_TYPES)
                                             .id(document.name))
                             .value();

    assertThat(exists).isFalse();
  }

  private void save(ComputingTypeDocument document) {

    try {
      client.create(save -> save.index(Constants.COMPUTING_TYPES)
                                .id(document.id)
                                .document(document)
                                .refresh(Refresh.True));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private ComputingTypeDocument loadType(String id) {

    try {
      return client.get(g -> g.index(Constants.COMPUTING_TYPES)
                              .id(id)
                              .refresh(true),
                        ComputingTypeDocument.class)
                   .source();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @BeforeEach
  public void clean() {

    try {
      client.deleteByQuery(d -> d.index(Constants.COMPUTING_TYPES)
                                 .query(Query.of(f -> f.matchAll(a -> a)))
                                 .refresh(true));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
