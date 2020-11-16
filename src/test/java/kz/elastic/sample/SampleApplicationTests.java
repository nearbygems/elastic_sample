package kz.elastic.sample;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.*;
import kz.elastic.sample.register.ActorRegister;
import kz.elastic.sample.util.Ids;
import kz.greetgo.util.RND;
import lombok.SneakyThrows;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@SpringBootTest
public class SampleApplicationTests {

  // region Autowired fields
  @Autowired
  private ActorRegister actorRegister;
  // endregion

  protected VoiceActor rndActor() {
    var ret = new VoiceActor();
    ret.id = Ids.generate();
    ret.name = RND.str(5);
    ret.surname = RND.str(5);
    return ret;
  }

  protected VoiceActor rndActor(Person person) {
    var ret = new VoiceActor();
    ret.id = Ids.generate();
    ret.name = RND.str(5);
    ret.surname = RND.str(5);
    person.actors.add(ret);
    return ret;
  }

  protected DevilFruit rndFruit() {
    var ret = new DevilFruit();
    ret.id = Ids.generate();
    ret.name = RND.str(5);
    ret.meaning = RND.str(5);
    ret.type = rndFruitType();
    return ret;
  }

  protected DevilFruit rndFruit(Person person) {
    var ret = new DevilFruit();
    ret.id = Ids.generate();
    ret.name = RND.str(5);
    ret.meaning = RND.str(5);
    ret.type = rndFruitType();
    person.fruits.add(ret);
    return ret;
  }

  protected Affiliation rndAffiliation() {
    var ret = new Affiliation();
    ret.id = Ids.generate();
    ret.name = RND.str(10);
    ret.captain = RND.str(10);
    ret.shipName = RND.str(10);
    ret.bounty = RND.plusLong(Long.MAX_VALUE);
    return ret;
  }

  protected Affiliation rndAffiliation(Person person) {
    var ret = new Affiliation();
    ret.id = Ids.generate();
    ret.name = RND.str(10);
    ret.captain = RND.str(10);
    ret.shipName = RND.str(10);
    ret.bounty = RND.plusLong(Long.MAX_VALUE);
    person.affiliations.add(ret);
    return ret;
  }

  protected Person rndPerson() {
    var person = new Person();
    person.id = Ids.generate();
    person.name = RND.str(7);
    person.alias = RND.str(8);
    person.epithet = RND.str(9);
    person.age = RND.plusInt(100);
    person.height = RND.plusInt(300);
    person.birthday = toLocalDate(RND.dateYears(0, 2000));
    person.bloodType = rndBloodType();
    person.bounty = RND.plusLong(Long.MAX_VALUE);
    person.status = rndStatus();
    return person;
  }

  @SneakyThrows
  protected String prettyJson(String jsonStr) {
    XContentBuilder b = XContentFactory.jsonBuilder().prettyPrint();
    var xContent = XContentFactory.xContent(XContentType.JSON);
    try (XContentParser p = xContent.createParser(NamedXContentRegistry.EMPTY, null, jsonStr)) {
      b.copyCurrentStructure(p);
    }
    return Strings.toString(b);
  }

  protected @NotNull Map<String, Object> castToMap(Object o) {
    requireNonNull(o);
    //noinspection unchecked
    return (Map<String, Object>) o;
  }

  protected void printSettings(Settings settings) {
    settings.keySet().stream()
      .sorted()
      .forEachOrdered(key -> System.out.println("  " + key + " = " + settings.get(key)));
  }

  @SneakyThrows
  protected void printMappings(MappingMetaData mappings) {
    System.out.println(prettyJson(mappings.source().toString()));
  }

  protected LocalDate toLocalDate(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  protected FruitType rndFruitType() {
    return FruitType.types().get(RND.plusInt(4));
  }

  protected BloodType rndBloodType() { return BloodType.types().get(RND.plusInt(3)); }

  protected Status rndStatus() { return Status.types().get(RND.plusInt(1)); }

  @AfterAll
  @SneakyThrows
  static void deleteIndexes() {
    for (var index : ElasticSearch.indexes()) {
      var request = HttpRequest.newBuilder(URI.create("http://localhost:9200/" + index))
        .DELETE()
        .build();
      HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofByteArray());
    }
  }

  @SneakyThrows
  protected void ins(VoiceActor... actors) {
    for (var actor : actors) {
      actorRegister.addActor(actor);
    }
  }

}
