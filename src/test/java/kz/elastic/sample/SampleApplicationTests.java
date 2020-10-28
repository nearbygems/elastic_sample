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

    person.name = RND.str(10);
    person.alias = RND.str(10);
    person.epithet = RND.str(10);

    person.age = RND.plusInt(100);
    person.height = RND.plusInt(300);
    person.birthday = toLocalDate(RND.dateYears(0, 2000));

    var affiliation = new Affiliation();
    affiliation.id = Ids.generate();
    affiliation.name = RND.str(10);
    affiliation.captain = RND.str(10);
    affiliation.shipName = RND.str(10);
    affiliation.bounty = RND.plusLong(Long.MAX_VALUE);

    person.affiliations.add(affiliation);

    person.actor = new VoiceActor();
    person.actor.id = Ids.generate();
    person.actor.surname = RND.str(10);
    person.actor.name = RND.str(10);

    person.bloodType = BloodType.XF;
    person.bounty = 320_000_000L;
    person.status = Status.ALIVE;

    return person;
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
