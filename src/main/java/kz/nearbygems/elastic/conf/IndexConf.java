package kz.nearbygems.elastic.conf;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import kz.nearbygems.elastic.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class IndexConf {

  private final ElasticsearchClient client;
  private final RestClient          restClient;

  @SneakyThrows
  private void createIndex(String name) {

    final var indexNotExists = !client.indices().exists(e -> e.index(name)).value();

    if (indexNotExists) {

      try (final var inputStream = inputStream(name)) {

        final var json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        performRequest("/" + name, json);
      }

    }

  }

  @SneakyThrows
  private void performRequest(String uri, String json) {

    final var request = new Request("PUT", uri);
    if (json != null) {
      request.setJsonEntity(json);
    }
    restClient.performRequest(request);
  }

  @SneakyThrows
  @PostConstruct
  public void init() {

    List.of(Constants.DATA_CATALOG,
            Constants.TAGS,
            Constants.PERSONS,
            Constants.STORAGES,
            Constants.COMPUTING_TYPES)
        .forEach(this :: createIndex);
  }

  @SneakyThrows
  private InputStream inputStream(String name) {

    return new ClassPathResource("index/" + name + ".json").getInputStream();
  }

}
