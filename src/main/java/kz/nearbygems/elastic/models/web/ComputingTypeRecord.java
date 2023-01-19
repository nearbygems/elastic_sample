package kz.nearbygems.elastic.models.web;

import kz.nearbygems.elastic.models.elastic.ComputingTypeDocument;
import kz.nearbygems.elastic.models.enums.DataType;
import kz.nearbygems.elastic.models.enums.SystemType;
import kz.nearbygems.elastic.util.Ids;
import lombok.ToString;


@ToString
public class ComputingTypeRecord {

  public String     id;
  public String     name;
  public SystemType system;
  public DataType   type;
  public String     description;
  public long       count;
  public boolean    isActual;

  public ComputingTypeDocument toDocument() {

    final var document = new ComputingTypeDocument();
    document.id          = id == null ? Ids.generate() : id;
    document.name        = name;
    document.system      = system;
    document.type        = type;
    document.description = description;
    document.isActual    = isActual;
    return document;
  }

}
