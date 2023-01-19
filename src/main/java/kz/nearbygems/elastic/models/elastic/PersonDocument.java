package kz.nearbygems.elastic.models.elastic;

import com.fasterxml.jackson.annotation.JsonInclude;
import kz.nearbygems.elastic.models.web.Persons;
import org.keycloak.representations.idm.UserRepresentation;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonDocument {

  public String id;
  public String email;
  public String firstName;
  public String lastName;
  public String username;

  public Persons.Record toRecord() {

    final var record = new Persons.Record();
    record.id        = id;
    record.email     = email;
    record.firstName = firstName;
    record.lastName  = lastName;
    record.username  = username;
    return record;
  }

  public boolean hasEmail() {

    return email != null && !email.isBlank();
  }

  public static PersonDocument from(UserRepresentation user) {

    final var person = new PersonDocument();
    person.id        = user.getId();
    person.email     = user.getEmail();
    person.firstName = user.getFirstName();
    person.lastName  = user.getLastName();
    person.username  = user.getUsername();
    return person;
  }

}
