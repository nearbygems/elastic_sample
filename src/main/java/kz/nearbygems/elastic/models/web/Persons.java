package kz.nearbygems.elastic.models.web;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.ResponseBody;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import kz.nearbygems.elastic.models.elastic.PersonDocument;
import kz.nearbygems.elastic.util.Ids;
import lombok.ToString;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


public class Persons {

  public List<Record> records;
  public long         totalHits;

  public static Persons fromSearchResponse(SearchResponse<PersonDocument> response) {

    final var persons = new Persons();

    persons.totalHits = Optional.ofNullable(response)
                                .map(ResponseBody :: hits)
                                .map(HitsMetadata :: total)
                                .map(TotalHits :: value)
                                .orElse(0L);

    persons.records = Optional.ofNullable(response)
                              .map(ResponseBody :: hits)
                              .map(r -> r.hits()
                                         .stream()
                                         .map(Hit :: source)
                                         .filter(Objects :: nonNull)
                                         .map(PersonDocument :: toRecord)
                                         .collect(Collectors.toList()))
                              .orElse(Collections.emptyList());

    return persons;
  }


  @ToString
  public static class Record {

    public String id;
    public String email;
    public String firstName;
    public String lastName;
    public String username;

    public PersonDocument toDocument() {

      final var document = new PersonDocument();
      document.id        = id == null ? Ids.generate() : id;
      document.email     = email;
      document.firstName = firstName;
      document.lastName  = lastName;
      document.username  = username;
      return document;
    }

  }

}
