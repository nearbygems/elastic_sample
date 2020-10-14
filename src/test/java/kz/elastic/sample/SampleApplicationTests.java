package kz.elastic.sample;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.Human;
import kz.elastic.sample.register.ElasticRegister;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SampleApplicationTests {

  @Autowired
  private ElasticRegister elasticRegister;

  @Test
  void addHuman() throws Exception {

    var human = new Human();
    human.id = "jfg1i038c1";
    human.name = "Bergen";
    human.surname = "Juanyshev";
    human.age = 26;
    human.birthDay = LocalDate.of(1994, 8, 27);

    //
    //
    elasticRegister.addHuman(human);
    //
    //

    var getIndexRequest = new GetIndexRequest(ElasticSearch.index());
    var getIndexResponse = ElasticSearch.client().indices().get(getIndexRequest, RequestOptions.DEFAULT);

    System.out.println("\n8uj6dX1D5e :: index settings:");
    printSettings(getIndexResponse.getSettings().get(ElasticSearch.index()));

    var indexMatchMappings = getIndexResponse.getMappings().get(ElasticSearch.index());
    System.out.println("\n9VtYCQwRxg :: index mappings:");
    printMappings(indexMatchMappings);

    var getRequest = new GetRequest(ElasticSearch.index());
    getRequest.id(human.id);
    var getResponse = ElasticSearch.client().get(getRequest, RequestOptions.DEFAULT);

    System.out.println("KbKj71IqrJ :: getResponse.source() = " + prettyJson(getResponse.getSourceAsString()));

    Map<String, Object> source = getResponse.getSource();
    var name = source.get(Human.ES_NAME);
    var surname = source.get(Human.ES_SURNAME);
    var age = source.get(Human.ES_AGE);
    var birthDay = source.get(Human.ES_BIRTH_DAY);

    assertThat(name).isEqualTo(human.name);
    assertThat(surname).isEqualTo(human.surname);
    assertThat(age).isEqualTo(human.age);
    assertThat(birthDay).isEqualTo(human.birthDay.toString());
  }

  protected void printSettings(Settings settings) {
    settings.keySet().stream().sorted()
      .forEachOrdered(key -> System.out.println("  " + key + " = " + settings.get(key)));
  }

  protected void printMappings(MappingMetaData mappings) throws Exception {
    var jsonStr = mappings.source().toString();

    System.out.println(prettyJson(jsonStr));
  }

  private String prettyJson(String jsonStr) throws IOException {
    XContentBuilder b = XContentFactory.jsonBuilder().prettyPrint();

    var xContent = XContentFactory.xContent(XContentType.JSON);

    try (XContentParser p = xContent.createParser(NamedXContentRegistry.EMPTY, null, jsonStr)) {
      b.copyCurrentStructure(p);
    }

    return Strings.toString(b);
  }

  private @NotNull Map<String, Object> castToMap(Object o) {
    requireNonNull(o);
    //noinspection unchecked
    return (Map<String, Object>) o;
  }

}
