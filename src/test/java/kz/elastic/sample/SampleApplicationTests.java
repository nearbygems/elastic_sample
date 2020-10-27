package kz.elastic.sample;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.*;
import kz.elastic.sample.register.PersonRegister;
import kz.elastic.sample.util.Ids;
import kz.greetgo.util.RND;
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
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@SpringBootTest
class SampleApplicationTests {

  // region Autowired fields
  @Autowired
  private PersonRegister personRegister;
  // endregion

  @Test
  void addPersonage() throws Exception {

    var personage = new Person();

    personage.id = Ids.generate();

    personage.name = RND.str(10);
    personage.alias = RND.str(10);
    personage.epithet = RND.str(10);

    personage.age = RND.plusInt(100);
    personage.height = RND.plusInt(300);
    personage.birthday = toLocalDate(RND.dateYears(0, 2000));

    var affiliation = new Affiliation();
    affiliation.id = Ids.generate();
    affiliation.name = RND.str(10);
    affiliation.captain = RND.str(10);
    affiliation.shipName = RND.str(10);
    affiliation.bounty = RND.plusLong(Long.MAX_VALUE);

    personage.affiliations.add(affiliation);

    personage.actor = new VoiceActor();
    personage.actor.id = Ids.generate();
    personage.actor.surname = RND.str(10);
    personage.actor.name = RND.str(10);

    personage.bloodType = BloodType.XF;
    personage.bounty = 320_000_000L;
    personage.status = Status.ALIVE;

    //
    //
    personRegister.addPerson(personage);
    //
    //

    var getIndexRequest = new GetIndexRequest(ElasticSearch.person());
    var getIndexResponse = ElasticSearch.client().indices().get(getIndexRequest, RequestOptions.DEFAULT);

    System.out.println("\n8uj6dX1D5e :: index settings:");
    printSettings(getIndexResponse.getSettings().get(ElasticSearch.person()));

    var indexMatchMappings = getIndexResponse.getMappings().get(ElasticSearch.person());
    System.out.println("\n9VtYCQwRxg :: index mappings:");
    printMappings(indexMatchMappings);

    var getRequest = new GetRequest(ElasticSearch.person());
    getRequest.id(personage.id);
    var getResponse = ElasticSearch.client().get(getRequest, RequestOptions.DEFAULT);

    System.out.println("KbKj71IqrJ :: getResponse.source() = " + prettyJson(getResponse.getSourceAsString()));
  }

  private void printSettings(Settings settings) {
    settings.keySet().stream().sorted()
      .forEachOrdered(key -> System.out.println("  " + key + " = " + settings.get(key)));
  }

  private void printMappings(MappingMetaData mappings) throws Exception {
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

  private LocalDate toLocalDate(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

}
