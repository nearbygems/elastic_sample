package kz.elastic.sample;

import kz.elastic.sample.register.PersonRegister;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class AsdTest extends SampleApplicationTests {

  // region Autowired fields
  @Autowired
  private PersonRegister personRegister;
  // endregion

  @Test
  void addPersonage() throws Exception {

    System.out.println("SUP");
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
