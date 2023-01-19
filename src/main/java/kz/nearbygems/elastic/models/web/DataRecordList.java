package kz.nearbygems.elastic.models.web;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import kz.nearbygems.elastic.models.elastic.CatalogDocument;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@ToString
public class DataRecordList {

  public List<DataCatalogRecord> records = new ArrayList<>();
  public long                    totalHits;
  public long                    tookInMillis;

  public static DataRecordList from(SearchResponse<CatalogDocument> response) {

    final var list = new DataRecordList();
    list.records      = response.hits().hits()
                                .stream()
                                .map(CatalogDocument :: from)
                                .filter(Objects :: nonNull)
                                .collect(Collectors.toList());
    list.totalHits    = Optional.ofNullable(response.hits().total())
                                .map(TotalHits :: value)
                                .orElse(0L);
    list.tookInMillis = response.took();
    return list;
  }

}
