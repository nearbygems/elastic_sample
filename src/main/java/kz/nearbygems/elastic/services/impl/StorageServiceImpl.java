package kz.nearbygems.elastic.services.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.search.Hit;
import kz.nearbygems.elastic.errors.NotFoundException;
import kz.nearbygems.elastic.models.elastic.ComputingTypeDocument;
import kz.nearbygems.elastic.models.elastic.StorageDocument;
import kz.nearbygems.elastic.models.web.StorageRecord;
import kz.nearbygems.elastic.services.StorageService;
import kz.nearbygems.elastic.util.Constants;
import kz.nearbygems.elastic.util.Elastic;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

  private final ElasticsearchClient client;

  @Override
  @SneakyThrows
  public List<StorageRecord> load() {

    return client.search(search -> search.index(Constants.STORAGES)
                                         .trackTotalHits(t -> t.enabled(true))
                                         .sort(sort -> sort.field(field -> field.field(Elastic.Fields.name)
                                                                                .order(SortOrder.Desc)))
                                         .from(0)
                                         .size(10_000),
                         StorageDocument.class)
                 .hits()
                 .hits()
                 .stream()
                 .map(Hit :: source)
                 .filter(Objects :: nonNull)
                 .map(StorageDocument :: toRecord)
                 .collect(Collectors.toList());
  }

  @Override
  @SneakyThrows
  public StorageRecord loadById(String id) {

    final var document = client.get(get -> get.index(Constants.STORAGES)
                                              .id(id),
                                    StorageDocument.class)
                               .source();

    return Optional.ofNullable(document)
                   .map(StorageDocument :: toRecord)
                   .orElseThrow(() -> new NotFoundException("There is no storage with an id = `" + id + "`"));
  }

  @Override
  @SneakyThrows
  public String save(StorageRecord record) {

    final var document = record.toDocument();

    return client.update(update -> update.index(Constants.STORAGES)
                                         .id(document.id)
                                         .docAsUpsert(true)
                                         .doc(document),
                         ComputingTypeDocument.class)
                 .id();
  }

  @Override
  @SneakyThrows
  public void delete(String id) {

    client.delete(delete -> delete.index(Constants.STORAGES)
                                  .id(id));
  }

}
