package kz.nearbygems.elastic.models.web;

import kz.nearbygems.elastic.models.elastic.StorageDocument;
import kz.nearbygems.elastic.util.Ids;
import lombok.ToString;


@ToString
public class StorageRecord {

  public String  id;
  public String  name;
  public boolean isActual;

  public StorageDocument toDocument() {

    final var document = new StorageDocument();
    document.id       = id == null ? Ids.generate() : id;
    document.name     = name;
    document.isActual = isActual;
    return document;
  }

}
