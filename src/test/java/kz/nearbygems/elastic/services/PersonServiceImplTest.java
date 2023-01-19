package kz.nearbygems.elastic.services;

import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import kz.nearbygems.elastic.CatalogApplicationTests;
import kz.nearbygems.elastic.models.elastic.PersonDocument;
import kz.nearbygems.elastic.models.web.Persons;
import kz.nearbygems.elastic.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class PersonServiceImplTest extends CatalogApplicationTests {

  @Autowired
  private PersonService service;

  @Test
  public void search() {

    final var search = "search";

    final var personEmail = new PersonDocument();
    personEmail.id       = "1";
    personEmail.email    = search + "email";
    personEmail.lastName = "lastName";
    save(personEmail);

    final var personFirstName = new PersonDocument();
    personFirstName.id        = "2";
    personFirstName.firstName = search + "firstName";
    personFirstName.lastName  = "lastName";
    save(personFirstName);

    final var personLastName = new PersonDocument();
    personLastName.id       = "3";
    personLastName.lastName = search + "lastName";
    save(personLastName);

    final var personUsername = new PersonDocument();
    personUsername.id       = "4";
    personUsername.username = search + "username";
    personUsername.lastName = "lastName";
    save(personUsername);

    final var personNo = new PersonDocument();
    personNo.id    = "no";
    personNo.email = "email";
    save(personNo);

    //
    final var persons = service.search(search, 0, 4);
    //

    assertThat(persons).isNotNull();
    assertThat(persons.totalHits).isEqualTo(4);
    assertThat(persons.records).hasSize(4);

    final var ids = persons.records.stream().map(r -> r.id).collect(Collectors.toList());

    assertThat(ids).contains(personEmail.id, personLastName.id, personFirstName.id, personUsername.id);
  }

  @Test
  public void loadById() {

    final var document = new PersonDocument();
    document.id        = "id";
    document.email     = "email";
    document.firstName = "firstname";
    document.lastName  = "lastname";
    document.username  = "username";

    save(document);

    //
    final var record = service.loadById(document.id);
    //

    assertThat(record).isNotNull();
    assertThat(document.id).isEqualTo(record.id);
    assertThat(document.email).isEqualTo(record.email);
    assertThat(document.firstName).isEqualTo(record.firstName);
    assertThat(document.lastName).isEqualTo(record.lastName);
    assertThat(document.username).isEqualTo(record.username);
  }

  @Test
  public void save() {

    final var record = new Persons.Record();
    record.id        = "id";
    record.email     = "email";
    record.firstName = "firstname";
    record.lastName  = "lastname";
    record.username  = "username";

    //
    service.save(record);
    //

    final var document = loadPerson(record.id);

    assertThat(document).isNotNull();
    assertThat(document.id).isEqualTo(record.id);
    assertThat(document.email).isEqualTo(record.email);
    assertThat(document.firstName).isEqualTo(record.firstName);
    assertThat(document.lastName).isEqualTo(record.lastName);
    assertThat(document.username).isEqualTo(record.username);
  }

  @Test
  public void delete() throws IOException {

    final var document = new PersonDocument();
    document.id = "id";

    save(document);

    //
    service.delete(document.id);
    //

    final var exists = client.exists(ex -> ex.index(Constants.PERSONS).id(document.id)).value();

    assertThat(exists).isFalse();
  }

  private void save(PersonDocument document) {

    try {
      client.create(save -> save.index(Constants.PERSONS).id(document.id).document(document).refresh(Refresh.True));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private PersonDocument loadPerson(String id) {

    try {
      return client.get(g -> g.index(Constants.PERSONS).id(id).refresh(true), PersonDocument.class).source();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @BeforeEach
  public void clean() {

    try {
      client.deleteByQuery(d -> d.index(Constants.PERSONS).query(Query.of(f -> f.matchAll(a -> a))).refresh(true));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
