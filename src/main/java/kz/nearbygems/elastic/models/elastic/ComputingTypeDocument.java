package kz.nearbygems.elastic.models.elastic;

import com.fasterxml.jackson.annotation.JsonInclude;
import kz.nearbygems.elastic.models.enums.DataType;
import kz.nearbygems.elastic.models.enums.SystemType;
import kz.nearbygems.elastic.models.web.ComputingTypeRecord;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComputingTypeDocument {

  public String     id;
  public String     name;
  public SystemType system;
  public DataType   type;
  public String     description;
  public boolean    isActual;

  public ComputingTypeRecord toRecord() {

    final var record = new ComputingTypeRecord();
    record.id          = id;
    record.name        = name;
    record.system      = system;
    record.type        = type;
    record.description = description;
    record.isActual    = isActual;
    return record;
  }

}
