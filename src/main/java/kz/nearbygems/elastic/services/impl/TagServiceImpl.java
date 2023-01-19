package kz.nearbygems.elastic.services.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.search.Hit;
import kz.nearbygems.elastic.errors.NotFoundException;
import kz.nearbygems.elastic.models.elastic.TagDocument;
import kz.nearbygems.elastic.models.web.TagRecord;
import kz.nearbygems.elastic.services.TagService;
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
public class TagServiceImpl implements TagService {

  private final ElasticsearchClient client;

  @Override
  @SneakyThrows
  public List<TagRecord> load() {

    return client.search(search -> search.index(Constants.TAGS)
                                         .trackTotalHits(t -> t.enabled(true))
                                         .sort(sort -> sort.field(field -> field.field(Elastic.Fields.name)
                                                                                .order(SortOrder.Desc)))
                                         .from(0)
                                         .size(10_000),
                         TagDocument.class)
                 .hits()
                 .hits()
                 .stream()
                 .map(Hit :: source)
                 .filter(Objects :: nonNull)
                 .map(TagDocument :: toRecord)
                 .collect(Collectors.toList());
  }

  @Override
  @SneakyThrows
  public TagRecord loadById(String id) {

    final var document = client.get(get -> get.index(Constants.TAGS)
                                              .id(id),
                                    TagDocument.class)
                               .source();

    return Optional.ofNullable(document)
                   .map(TagDocument :: toRecord)
                   .orElseThrow(() -> new NotFoundException("There is no tag with an id = `" + id + "`"));
  }

  @Override
  @SneakyThrows
  public String save(TagRecord record) {

    final var document = record.toDocument();

    return client.update(update -> update.index(Constants.TAGS)
                                         .id(document.id)
                                         .docAsUpsert(true)
                                         .doc(document),
                         TagDocument.class)
                 .id();
  }

  @Override
  @SneakyThrows
  public void delete(String id) {

    client.delete(delete -> delete.index(Constants.TAGS)
                                  .id(id));
  }

}
