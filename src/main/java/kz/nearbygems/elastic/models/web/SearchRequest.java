package kz.nearbygems.elastic.models.web;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kz.nearbygems.elastic.util.Elastic;
import kz.nearbygems.elastic.util.Json;
import lombok.ToString;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRequest {

  public String       search;
  public Page         page;
  public Sort         sort;
  public List<Filter> filters;

  public int from() {

    return Optional.ofNullable(page)
                   .map(p -> p.offset)
                   .orElse(0);
  }

  public int size() {

    return Optional.ofNullable(page)
                   .map(p -> p.limit)
                   .orElse(10);
  }

  public List<SortOptions> sort() {

    final var options = Optional.ofNullable(sort)
                                .map(Sort :: options)
                                .orElse(Sort.nameAsc());

    return List.of(options);
  }

  public Query query() {

    if (search != null) {
      return BoolQuery.of(b -> b.filter(filters())
                                .should(textFields())
                                .should(personFields())
                                .should(stagingFields())
                                .should(stagingColumns())
                                .should(stagingTransfers()))
                      ._toQuery();
    } else {
      return BoolQuery.of(b -> b.filter(filters()))
                      ._toQuery();
    }

  }

  public static SearchRequest search(String search) {

    final var req = new SearchRequest();
    req.search = search;
    return req;
  }

  private List<Query> textFields() {

    return List.of(text(Elastic.Fields.name),
                   text(Elastic.Fields.server),
                   text(Elastic.Fields.comment),
                   text(Elastic.Fields.description));
  }

  private List<Query> personFields() {

    return List.of(person(Elastic.Fields.businessClients),
                   person(Elastic.Fields.analysts),
                   person(Elastic.Fields.managers),
                   person(Elastic.Fields.developers),
                   person(Elastic.Fields.responsible),
                   person(Elastic.Fields.owners));
  }

  private Query stagingFields() {

    final var name           = text(Json.dot(Elastic.Fields.stagings, Elastic.Fields.name));
    final var dataSourceName = text(Json.dot(Elastic.Fields.stagings, Elastic.Fields.dataSourceName));
    final var description    = text(Json.dot(Elastic.Fields.stagings, Elastic.Fields.description));

    return NestedQuery.of(n -> n.path(Elastic.Fields.stagings)
                                .innerHits(i -> i.name(Elastic.Fields.stagings))
                                .query(q -> q.bool(b -> b.should(name)
                                                         .should(dataSourceName)
                                                         .should(description))))
                      ._toQuery();
  }

  private Query stagingColumns() {

    final var name        = text(Json.dot(Elastic.Fields.stagings, Elastic.Fields.columns, Elastic.Fields.name));
    final var description = text(Json.dot(Elastic.Fields.stagings, Elastic.Fields.columns, Elastic.Fields.description));

    final var columns = Json.dot(Elastic.Fields.stagings, Elastic.Fields.columns);

    return NestedQuery.of(n -> n.path(Elastic.Fields.stagings)
                                .innerHits(in -> in.name(Elastic.Fields.columns)
                                                   .fields(Elastic.Fields.name))
                                .query(q -> q.nested(ne -> ne.path(columns)
                                                             .query(qu -> qu.bool(b -> b.should(name)
                                                                                        .should(description))))))
                      ._toQuery();
  }

  private List<Query> stagingTransfers() {

    return List.of(transfers(Elastic.Fields.outputs),
                   transfers(Elastic.Fields.inputs));
  }

  private Query transfers(String field) {

    final var system  = text(Json.dot(Elastic.Fields.stagings, field, Elastic.Fields.system));
    final var storage = text(Json.dot(Elastic.Fields.stagings, field, Elastic.Fields.storage));
    final var host    = text(Json.dot(Elastic.Fields.stagings, field, Elastic.Fields.host));

    final var transfer = Json.dot(Elastic.Fields.stagings, field);

    return NestedQuery.of(n -> n.path(Elastic.Fields.stagings)
                                .innerHits(in -> in.name(field)
                                                   .fields(Elastic.Fields.name))
                                .query(q -> q.nested(ne -> ne.path(transfer)
                                                             .query(qu -> qu.bool(b -> b.should(system)
                                                                                        .should(storage)
                                                                                        .should(host))))))
                      ._toQuery();
  }

  private Query text(String field) {

    final var match = MatchQuery.of(m -> m.field(field)
                                          .query(search))
                                ._toQuery();

    final var phrase = MatchPhrasePrefixQuery.of(m -> m.field(field)
                                                       .query(search))
                                             ._toQuery();

    return BoolQuery.of(b -> b.should(match)
                              .should(phrase))
                    ._toQuery();
  }

  private Query person(String field) {

    final var email     = text(Json.dot(field, Elastic.Fields.email));
    final var lastName  = text(Json.dot(field, Elastic.Fields.lastName));
    final var firstName = text(Json.dot(field, Elastic.Fields.firstName));

    return NestedQuery.of(n -> n.path(field)
                                .query(q -> q.bool(b -> b.should(email)
                                                         .should(lastName)
                                                         .should(firstName))))
                      ._toQuery();
  }

  private List<Query> filters() {

    return Optional.ofNullable(filters)
                   .stream()
                   .flatMap(Collection :: stream)
                   .map(this :: filter)
                   .collect(Collectors.toList());
  }

  private Query filter(Filter filter) {

    if (filter.ids != null) {
      return TermsQuery.of(t -> t.field(filter.field)
                                 .terms(terms -> terms.value(filter.values())))
                       ._toQuery();
    }

    if (filter.isDateRange()) {
      return RangeQuery.of(r -> r.field(filter.field)
                                 .gte(JsonData.of(filter.dateFrom))
                                 .lte(JsonData.of(filter.dateTo)))
                       ._toQuery();
    }

    if (filter.isLongRange()) {
      return RangeQuery.of(r -> r.field(filter.field)
                                 .gte(JsonData.of(filter.longFrom))
                                 .lte(JsonData.of(filter.longTo)))
                       ._toQuery();
    }

    return MatchAllQuery.of(m -> m)._toQuery();
  }

}
