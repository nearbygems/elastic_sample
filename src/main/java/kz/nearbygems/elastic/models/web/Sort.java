package kz.nearbygems.elastic.models.web;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import kz.nearbygems.elastic.util.Elastic;
import kz.nearbygems.elastic.util.Json;


public class Sort {

  public String    field;
  public SortOrder order;

  public static Sort of(String fieldName, SortOrder sortOrder) {

    final var ret = new Sort();
    ret.field = fieldName;
    ret.order = sortOrder;
    return ret;
  }

  public SortOptions options() {

    if (field.equals(Elastic.Fields.createdAt) || field.equals(Elastic.Fields.lastModifiedAt)) {
      return SortOptions.of(s -> s.field(f -> f.field(field)
                                               .order(order)));
    }

    return SortOptions.of(s -> s.field(f -> f.field(Json.dot(field, Elastic.Fields.raw))
                                             .order(order)));

  }

  public static SortOptions nameAsc() {

    return SortOptions.of(s -> s.field(f -> f.field(Json.dot(Elastic.Fields.name, Elastic.Fields.raw))
                                             .order(SortOrder.Asc)));

  }

}
