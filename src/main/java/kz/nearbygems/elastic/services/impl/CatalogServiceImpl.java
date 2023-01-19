package kz.nearbygems.elastic.services.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import kz.nearbygems.elastic.errors.NotFoundException;
import kz.nearbygems.elastic.models.elastic.CatalogDocument;
import kz.nearbygems.elastic.models.web.DataCatalogRecord;
import kz.nearbygems.elastic.models.web.DataRecordList;
import kz.nearbygems.elastic.models.web.SearchRequest;
import kz.nearbygems.elastic.services.CatalogService;
import kz.nearbygems.elastic.util.Constants;
import kz.nearbygems.elastic.util.Elastic;
import kz.nearbygems.elastic.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

  private final ElasticsearchClient client;

  @Override
  @SneakyThrows
  public DataRecordList search(SearchRequest request) {

    final var resp = client.search(s -> s.index(Constants.DATA_CATALOG)
                                         .trackTotalHits(t -> t.enabled(true))
                                         .sort(request.sort())
                                         .from(request.from())
                                         .size(request.size())
                                         .query(request.query()),
                                   CatalogDocument.class);

    return DataRecordList.from(resp);
  }

  @Override
  @SneakyThrows
  public long count(DataCatalogRecord.DocTypeRecord docType) {

    final var type    = Json.dot(Elastic.Fields.docType, Elastic.Fields.type);
    final var subType = Json.dot(Elastic.Fields.docType, Elastic.Fields.subType);

    return client.count(c -> c.index(Constants.DATA_CATALOG)
                              .query(q -> q.bool(b -> b.must(m -> m.term(t -> t.field(type).value(docType.type.name())))
                                                       .must(m -> m.term(t -> t.field(subType)
                                                                               .value(docType.subType)))))).count();
  }

  @Override
  @SneakyThrows
  public void delete(String id) {

    client.delete(delete -> delete.index(Constants.DATA_CATALOG).id(id));
  }

  @Override
  @SneakyThrows
  public List<DataCatalogRecord.StagingRecord.ColumnRecord> loadFields(String id, String name) {

    return loadById(id).stagings.stream()
                                .filter(s -> s.name.equals(name))
                                .findAny()
                                .map(s -> s.columns)
                                .orElse(Collections.emptyList());
  }

  @SneakyThrows
  private DataCatalogRecord loadById(String id) {

    final var document = client.get(get -> get.index(Constants.DATA_CATALOG).id(id), CatalogDocument.class)
                               .source();

    return Optional.ofNullable(document)
                   .map(CatalogDocument :: toRecord)
                   .orElseThrow(() -> new NotFoundException("No datamart with id = `" + id + "`"));
  }

}
