package kz.nearbygems.elastic.services.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import kz.nearbygems.elastic.errors.NotFoundException;
import kz.nearbygems.elastic.models.elastic.CatalogDocument;
import kz.nearbygems.elastic.models.web.DatamartRecord;
import kz.nearbygems.elastic.services.DatamartService;
import kz.nearbygems.elastic.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class DatamartServiceImpl implements DatamartService {

  private final ElasticsearchClient client;

  @Override
  @SneakyThrows
  public DatamartRecord loadById(String id) {

    final var document = client.get(get -> get.index(Constants.DATA_CATALOG)
                                              .id(id),
                                    CatalogDocument.class)
                               .source();

    return Optional.ofNullable(document)
                   .map(CatalogDocument :: toDatamart)
                   .orElseThrow(() -> new NotFoundException("No datamart with id = `" + id + "`"));
  }

  @Override
  @SneakyThrows
  public String save(DatamartRecord record) {

    final var document = record.toDocument();

    return client.update(update -> update.index(Constants.DATA_CATALOG)
                                         .id(document.id)
                                         .docAsUpsert(true)
                                         .doc(document),
                         CatalogDocument.class)
                 .id();
  }

}
