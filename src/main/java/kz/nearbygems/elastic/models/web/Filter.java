package kz.nearbygems.elastic.models.web;

import co.elastic.clients.elasticsearch._types.FieldValue;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class Filter {

  public String field;

  public List<String> ids;
  public Long         longFrom;
  public Long         longTo;
  public Date         dateFrom;
  public Date         dateTo;

  public List<FieldValue> values() {

    return Optional.ofNullable(ids)
                   .map(ids -> ids.stream().map(FieldValue :: of)
                                  .collect(Collectors.toList()))
                   .orElse(Collections.emptyList());
  }

  public boolean isDateRange() {

    return dateFrom != null && dateTo != null;
  }

  public boolean isLongRange() {

    return dateFrom != null && dateTo != null;
  }

}
