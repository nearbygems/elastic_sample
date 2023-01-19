package kz.nearbygems.elastic.models.elastic;

import com.fasterxml.jackson.annotation.JsonInclude;
import kz.nearbygems.elastic.models.web.TagRecord;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDocument {

  public String  id;
  public String  name;
  public boolean isActual;

  public TagRecord toRecord() {

    final var record = new TagRecord();
    record.id       = id;
    record.name     = name;
    record.isActual = isActual;
    return record;
  }

}
