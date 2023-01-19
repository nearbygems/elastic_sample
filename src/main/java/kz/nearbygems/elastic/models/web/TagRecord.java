package kz.nearbygems.elastic.models.web;

import kz.nearbygems.elastic.models.elastic.TagDocument;
import kz.nearbygems.elastic.util.Ids;
import lombok.ToString;


@ToString
public class TagRecord {

  public String  id;
  public String  name;
  public boolean isActual;

  public TagDocument toDocument() {

    final var dto = new TagDocument();
    dto.id       = id == null ? Ids.generate() : id;
    dto.name     = name;
    dto.isActual = isActual;
    return dto;
  }

}
