package kz.elastic.sample;

import kz.elastic.sample.model.*;
import kz.elastic.sample.util.Ids;
import kz.greetgo.util.RND;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@SpringBootTest
class SampleApplicationTests {

  protected Person rndPerson() {
    var person = new Person();
    person.id = Ids.generate();
    person.name = RND.str(7);
    person.alias = RND.str(8);
    person.epithet = RND.str(9);
    person.age = RND.plusInt(100);
    person.height = RND.plusInt(300);
    person.birthday = toLocalDate(RND.dateYears(0, 2000));
    person.affiliations.add(rndAffiliation());
    person.affiliations.add(rndAffiliation());
    person.affiliations.add(rndAffiliation());
    person.bloodType = BloodType.XF;
    person.bounty = 320_000_000L;
    person.status = Status.ALIVE;
    person.actors.add(rndActor());
    person.actors.add(rndActor());
    person.actors.add(rndActor());
    person.fruit = rndFruit();
    return person;
  }

  protected VoiceActor rndActor() {
    var ret = new VoiceActor();
    ret.id = Ids.generate();
    ret.name = RND.str(5);
    ret.surname = RND.str(5);
    return ret;
  }

  protected DevilFruit rndFruit() {
    var ret = new DevilFruit();
    ret.id = Ids.generate();
    ret.name = RND.str(5);
    ret.meaning = RND.str(5);
    ret.type = rndType();
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

  protected FruitType rndType() {
    return FruitType.types().get(RND.plusInt(4));
  }

  protected void printSettings(Settings settings) {
    settings.keySet().stream().sorted()
      .forEachOrdered(key -> System.out.println("  " + key + " = " + settings.get(key)));
  }

  protected void printMappings(MappingMetaData mappings) throws Exception {
    var jsonStr = mappings.source().toString();
    System.out.println(prettyJson(jsonStr));
  }

  protected String prettyJson(String jsonStr) throws IOException {
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

  protected LocalDate toLocalDate(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

}
