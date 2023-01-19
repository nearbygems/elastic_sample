package kz.nearbygems.elastic.models.elastic;

import com.fasterxml.jackson.annotation.JsonInclude;
import kz.nearbygems.elastic.models.web.StorageRecord;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class StorageDocument {

  public String  id;
  public String  name;
  public boolean isActual;

  public StorageRecord toRecord() {

    final var record = new StorageRecord();
    record.id       = id;
    record.name     = name;
    record.isActual = isActual;
    return record;
  }

}
