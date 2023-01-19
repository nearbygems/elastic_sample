package kz.nearbygems.elastic.services.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhrasePrefixQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import kz.nearbygems.elastic.conf.KeycloakProperties;
import kz.nearbygems.elastic.errors.NotFoundException;
import kz.nearbygems.elastic.models.elastic.PersonDocument;
import kz.nearbygems.elastic.models.web.Persons;
import kz.nearbygems.elastic.services.PersonService;
import kz.nearbygems.elastic.util.Constants;
import kz.nearbygems.elastic.util.Elastic;
import kz.nearbygems.elastic.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

  private final ElasticsearchClient client;
  private final KeycloakProperties  properties;

  @Override
  @SneakyThrows
  public void loadPersonsFromLdap() {

    final var rest = new ResteasyClientBuilder()
      .disableTrustManager()
      .connectionPoolSize(1)
      .build();

    try (final var keycloak = KeycloakBuilder.builder()
                                             .serverUrl(properties.getUrl())
                                             .grantType(OAuth2Constants.PASSWORD)
                                             .realm(properties.getRealm())
                                             .clientId(properties.getId())
                                             .clientSecret(properties.getSecret())
                                             .username(properties.getUsername())
                                             .password(properties.getPassword())
                                             .resteasyClient(rest)
                                             .build()) {

      final var count = keycloak.realm(properties.getRealm()).users().count();

      var       from = 0;
      final var size = 100;

      while (from < count) {

        final var bulk = new BulkRequest.Builder();

        keycloak.realm(properties.getRealm()).users()
                .list(from, size)
                .stream()
                .filter(UserRepresentation :: isEnabled)
                .map(PersonDocument :: from)
                .filter(PersonDocument :: hasEmail)
                .forEach(person ->
                           bulk.operations(operation ->
                                             operation.update(update ->
                                                                update.index(Constants.PERSONS)
                                                                      .id(person.id)
                                                                      .action(action -> action.docAsUpsert(true)
                                                                                              .doc(person)))));

        final var bulkResponse = client.bulk(bulk.build());

        if (bulkResponse.errors()) {
          bulkResponse.items()
                      .stream()
                      .map(BulkResponseItem :: error)
                      .filter(Objects :: nonNull)
                      .map(Objects :: toString)
                      .forEach(log :: error);
        }

        from += size;
      }

    }

  }

  @Override
  @SneakyThrows
  public Persons search(String text, int offset, int limit) {

    final var query = BoolQuery.of(bool -> bool.should(matchPhrasePrefixQuery(Elastic.Fields.email, text))
                                               .should(matchPhrasePrefixQuery(Elastic.Fields.lastName, text))
                                               .should(matchPhrasePrefixQuery(Elastic.Fields.firstName, text))
                                               .should(matchPhrasePrefixQuery(Elastic.Fields.username, text)))
                               ._toQuery();

    final var sort = SortOptions.of(s -> s.field(field -> field.field(Json.dot(Elastic.Fields.lastName,
                                                                               Elastic.Fields.raw))
                                                               .order(SortOrder.Desc)));

    final var searchResponse = client.search(search -> search.index(Constants.PERSONS)
                                                             .trackTotalHits(t -> t.enabled(true))
                                                             .query(query)
                                                             .sort(sort)
                                                             .from(offset)
                                                             .size(limit),
                                             PersonDocument.class);

    return Persons.fromSearchResponse(searchResponse);
  }

  private Query matchPhrasePrefixQuery(String field, String query) {

    return MatchPhrasePrefixQuery.of(match -> match.field(field)
                                                   .query(query))
                                 ._toQuery();
  }

  @Override
  @SneakyThrows
  public Persons.Record loadById(String id) {

    final var document = client.get(get -> get.index(Constants.PERSONS)
                                              .id(id),
                                    PersonDocument.class)
                               .source();

    return Optional.ofNullable(document)
                   .map(PersonDocument :: toRecord)
                   .orElseThrow(() -> new NotFoundException("There is no person with an id = `" + id + "`"));
  }

  @Override
  @SneakyThrows
  public String save(Persons.Record record) {

    final var document = record.toDocument();

    return client.update(update -> update.index(Constants.PERSONS)
                                         .id(document.id)
                                         .docAsUpsert(true)
                                         .doc(document),
                         PersonDocument.class)
                 .id();
  }

  @Override
  @SneakyThrows
  public void delete(String id) {

    client.delete(delete -> delete.index(Constants.PERSONS)
                                  .id(id));
  }

}
