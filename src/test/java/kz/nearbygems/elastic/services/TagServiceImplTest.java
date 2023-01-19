package kz.nearbygems.elastic.services;

import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import kz.nearbygems.elastic.CatalogApplicationTests;
import kz.nearbygems.elastic.models.elastic.TagDocument;
import kz.nearbygems.elastic.models.web.TagRecord;
import kz.nearbygems.elastic.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class TagServiceImplTest extends CatalogApplicationTests {

  @Autowired
  private TagService service;

  @Test
  public void load() {

    final var document1 = new TagDocument();
    document1.id   = "1";
    document1.name = "nane1";
    save(document1);

    final var document2 = new TagDocument();
    document2.id   = "2";
    document2.name = "nane2";
    save(document2);

    //
    final var storages = service.load();
    //

    assertThat(storages).isNotNull();
    assertThat(storages).hasSize(2);
  }

  @Test
  public void loadById() {

    final var document = new TagDocument();
    document.id       = "id";
    document.name     = "name";
    document.isActual = true;

    save(document);

    //
    final var record = service.loadById(document.id);
    //

    assertThat(record).isNotNull();
    assertThat(record.id).isEqualTo(document.id);
    assertThat(record.name).isEqualTo(document.name);
    assertThat(record.isActual).isTrue();
  }

  @Test
  public void save() {

    final var record = new TagRecord();
    record.id       = "id";
    record.name     = "name";
    record.isActual = true;

    //
    service.save(record);
    //

    final var document = loadTag(record.id);

    assertThat(document).isNotNull();
    assertThat(document.id).isEqualTo(record.id);
    assertThat(document.name).isEqualTo(record.name);
    assertThat(document.isActual).isTrue();
  }

  @Test
  public void delete() throws IOException {

    final var document = new TagDocument();
    document.id = "id";

    save(document);

    //
    service.delete(document.id);
    //

    final var exists = client.exists(ex -> ex.index(Constants.TAGS)
                                             .id(document.id))
                             .value();

    assertThat(exists).isFalse();
  }

  private void save(TagDocument document) {

    try {
      client.create(save -> save.index(Constants.TAGS).id(document.id).document(document).refresh(Refresh.True));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private TagDocument loadTag(String id) {

    try {
      return client.get(g -> g.index(Constants.TAGS).id(id).refresh(true), TagDocument.class).source();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @BeforeEach
  public void clean() {

    try {
      client.deleteByQuery(d -> d.index(Constants.TAGS).query(Query.of(f -> f.matchAll(a -> a))).refresh(true));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
