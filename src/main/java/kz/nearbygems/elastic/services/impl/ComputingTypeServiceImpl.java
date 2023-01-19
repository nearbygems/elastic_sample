package kz.nearbygems.elastic.services.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.search.Hit;
import kz.nearbygems.elastic.errors.NotFoundException;
import kz.nearbygems.elastic.models.elastic.ComputingTypeDocument;
import kz.nearbygems.elastic.models.enums.SystemType;
import kz.nearbygems.elastic.models.web.ComputingTypeRecord;
import kz.nearbygems.elastic.models.web.DataCatalogRecord;
import kz.nearbygems.elastic.services.CatalogService;
import kz.nearbygems.elastic.services.ComputingTypeService;
import kz.nearbygems.elastic.util.Constants;
import kz.nearbygems.elastic.util.Elastic;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ComputingTypeServiceImpl implements ComputingTypeService {

  private final ElasticsearchClient client;
  private final CatalogService      service;

  @Override
  @SneakyThrows
  public List<ComputingTypeRecord> load(SystemType system) {

    return client.search(search -> search.index(Constants.COMPUTING_TYPES)
                                         .trackTotalHits(t -> t.enabled(true))
                                         .query(query -> query.term(term -> term.field(Elastic.Fields.system)
                                                                                .value(system.name())))
                                         .sort(sort -> sort.field(field -> field.field(Elastic.Fields.name)
                                                                                .order(SortOrder.Desc))),
                         ComputingTypeDocument.class)
                 .hits()
                 .hits()
                 .stream()
                 .map(Hit :: source)
                 .filter(Objects :: nonNull)
                 .map(ComputingTypeDocument :: toRecord)
                 .peek(record -> record.count = service.count(DataCatalogRecord.DocTypeRecord.from(record)))
                 .collect(Collectors.toList());
  }

  @Override
  @SneakyThrows
  public ComputingTypeRecord loadById(String id) {

    final var document = client.get(get -> get.index(Constants.COMPUTING_TYPES)
                                              .id(id),
                                    ComputingTypeDocument.class)
                               .source();

    return Optional.ofNullable(document)
                   .map(ComputingTypeDocument :: toRecord)
                   .orElseThrow(() -> new NotFoundException("There is no computing type with an id = `" + id + "`"));
  }

  @Override
  @SneakyThrows
  public String save(ComputingTypeRecord record) {

    final var document = record.toDocument();

    return client.update(update -> update.index(Constants.COMPUTING_TYPES)
                                         .id(document.id)
                                         .docAsUpsert(true)
                                         .doc(document),
                         ComputingTypeDocument.class)
                 .id();
  }

  @Override
  @SneakyThrows
  public void delete(String id) {

    client.delete(delete -> delete.index(Constants.COMPUTING_TYPES)
                                  .id(id));
  }

}
