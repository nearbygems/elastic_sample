package kz.nearbygems.elastic.models.web;

import kz.nearbygems.elastic.models.elastic.CatalogDocument;
import kz.nearbygems.elastic.util.Ids;
import lombok.ToString;

import java.util.Optional;
import java.util.stream.Collectors;


@ToString
public class DatamartRecord extends DataCatalogRecord {

  public String path;
  public String gitUrl;
  public String confluenceUrl;
  public String storageTime;
  public String schedule;

  public CatalogDocument toDocument() {

    final var document = new CatalogDocument();
    document.id              = id == null ? Ids.generate() : id;
    document.name            = name;
    document.server          = server;
    document.storageTime     = storageTime;
    document.schedule        = schedule;
    document.path            = path;
    document.gitUrl          = gitUrl;
    document.confluenceUrl   = confluenceUrl;
    document.dagId           = dagId;
    document.docType         = Optional.ofNullable(docType)
                                       .map(DocTypeRecord :: toDocument)
                                       .orElse(null);
    document.description     = description;
    document.system          = system;
    document.periodicity     = periodicity;
    document.releaseStage    = releaseStage;
    document.isActual        = isActual;
    document.tags            = tags;
    document.createdAt       = createdAt;
    document.lastModifiedAt  = lastModifiedAt;
    document.stagings        = stagings.stream()
                                       .map(StagingRecord :: toDocument)
                                       .collect(Collectors.toList());
    document.businessClients = businessClients.stream()
                                              .map(PersonRecord :: toDocument)
                                              .collect(Collectors.toList());
    document.analysts        = analysts.stream()
                                       .map(PersonRecord :: toDocument)
                                       .collect(Collectors.toList());
    document.managers        = managers.stream()
                                       .map(PersonRecord :: toDocument)
                                       .collect(Collectors.toList());
    document.developers      = developers.stream()
                                         .map(PersonRecord :: toDocument)
                                         .collect(Collectors.toList());
    document.responsible     = responsible.stream()
                                          .map(PersonRecord :: toDocument)
                                          .collect(Collectors.toList());
    document.owners          = owners.stream()
                                     .map(PersonRecord :: toDocument)
                                     .collect(Collectors.toList());
    return document;
  }

}
