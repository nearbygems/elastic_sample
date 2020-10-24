package kz.elastic.sample;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.*;
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

@SpringBootTest
class SampleApplicationTests {

  // region Autowired fields
  @Autowired
  private ElasticRegister elasticRegister;
  // endregion

  @Test
  void addPersonage() throws Exception {

    var personage = new Personage();

    personage.id = "h8enwt9fqp";

    personage.name = "Roronoa Zoro";
    personage.alias = "Zoro-juurou";
    personage.epithet = "Pirate Hunter Zoro";

    personage.age = 21;
    personage.height = 181;
    personage.birthday = LocalDate.of(1994, 11, 11);

    var affiliation = new Affiliation();
    affiliation.id = "eug08jvluo";
    affiliation.name = "Straw Hat Pirates";
    affiliation.captain = "Monkey D. Luffy";
    affiliation.shipName = "Thousand Sunny";
    affiliation.bounty = 3_161_000_100L;

    personage.affiliations.add(affiliation);

    personage.actor = new VoiceActor();
    personage.actor.surname = "Nakai";
    personage.actor.name = "Kazuya";

    personage.bloodType = BloodType.XF;
    personage.bounty = 320_000_000L;
    personage.status = Status.ALIVE;

    //
    //
    elasticRegister.addPersonage(personage);
    //
    //

    var getIndexRequest = new GetIndexRequest(ElasticSearch.personage());
    var getIndexResponse = ElasticSearch.client().indices().get(getIndexRequest, RequestOptions.DEFAULT);

    System.out.println("\n8uj6dX1D5e :: index settings:");
    printSettings(getIndexResponse.getSettings().get(ElasticSearch.personage()));

    var indexMatchMappings = getIndexResponse.getMappings().get(ElasticSearch.personage());
    System.out.println("\n9VtYCQwRxg :: index mappings:");
    printMappings(indexMatchMappings);

    var getRequest = new GetRequest(ElasticSearch.personage());
    getRequest.id(personage.id);
    var getResponse = ElasticSearch.client().get(getRequest, RequestOptions.DEFAULT);

    System.out.println("KbKj71IqrJ :: getResponse.source() = " + prettyJson(getResponse.getSourceAsString()));
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
