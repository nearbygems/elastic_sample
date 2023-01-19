package kz.nearbygems.elastic.models.elastic;

import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.InnerHitsResult;
import co.elastic.clients.json.JsonData;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.json.JsonValue;
import kz.nearbygems.elastic.models.enums.*;
import kz.nearbygems.elastic.models.web.DataCatalogRecord;
import kz.nearbygems.elastic.models.web.DatamartRecord;
import kz.nearbygems.elastic.models.web.SourceRecord;
import kz.nearbygems.elastic.models.web.TriggerRecord;
import kz.nearbygems.elastic.util.Elastic;
import lombok.EqualsAndHashCode;

import java.util.*;
import java.util.stream.Collectors;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CatalogDocument {

  public String       id;
  public String       name;
  public String       server;
  public String       comment;
  public String       jiraTicket;
  public String       storageTime;
  public String       startTime;
  public String       schedule;
  public String       path;
  public String       gitUrl;
  public String       confluenceUrl;
  public long         dagId;
  public long         repeatQuantity;
  public DocType      docType;
  public String       description;
  public SystemType   system;
  public Periodicity  periodicity;
  public ReleaseStage releaseStage;
  public TriggerType  triggerType;
  public boolean      isActive;
  public boolean      isActual;
  public String[]     tags;
  public Date         createdAt;
  public Date         lastModifiedAt;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<Staging> stagings;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<Person>  businessClients;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<Person>  analysts;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<Person>  managers;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<Person>  developers;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<Person>  responsible;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<Person>  owners;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<String>  parents;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<String>  children;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Staging {

    public String         name;
    public long           workId;
    public String         description;
    public String         dataSourceName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<Transfer> outputs = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<Transfer> inputs  = new ArrayList<>();
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<Column>   columns = new ArrayList<>();


    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Transfer {

      public String system;
      public String storage;
      public String host;
      public String port;

      public DataCatalogRecord.StagingRecord.TransferRecord toRecord() {

        final var record = new DataCatalogRecord.StagingRecord.TransferRecord();
        record.system  = system;
        record.storage = storage;
        record.host    = host;
        record.port    = port;
        return record;
      }

    }


    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Column {

      public String name;
      public String description;
      public String fieldType;
      public long   orderIndex;

      public DataCatalogRecord.StagingRecord.ColumnRecord toRecord() {

        final var record = new DataCatalogRecord.StagingRecord.ColumnRecord();
        record.name        = name;
        record.description = description;
        record.fieldType   = fieldType;
        record.orderIndex  = orderIndex;
        return record;
      }

    }

    public DataCatalogRecord.StagingRecord toRecord() {

      final var record = new DataCatalogRecord.StagingRecord();
      record.name           = name;
      record.workId         = workId;
      record.description    = description;
      record.dataSourceName = dataSourceName;
      record.outputs        = outputs.stream()
                                     .map(Transfer :: toRecord)
                                     .collect(Collectors.toList());
      record.inputs         = inputs.stream()
                                    .map(Transfer :: toRecord)
                                    .collect(Collectors.toList());
      record.columns        = columns.stream()
                                     .map(Column :: toRecord)
                                     .collect(Collectors.toList());
      return record;
    }

  }

  @EqualsAndHashCode
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class DocType {

    public DataType type;
    public String   subType;

    public DataCatalogRecord.DocTypeRecord toRecord() {

      final var record = new DataCatalogRecord.DocTypeRecord();
      record.type    = type;
      record.subType = subType;
      return record;
    }

  }

  @EqualsAndHashCode
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Person {

    public String email;
    public String lastName;
    public String firstName;

    public DataCatalogRecord.PersonRecord toRecord() {

      final var record = new DataCatalogRecord.PersonRecord();
      record.email     = email;
      record.lastName  = lastName;
      record.firstName = firstName;
      return record;
    }

  }

  public DataCatalogRecord toRecord() {

    final var record = new DataCatalogRecord();
    record.id              = id;
    record.name            = name;
    record.server          = server;
    record.dagId           = dagId;
    record.docType         = Optional.ofNullable(docType)
                                     .map(DocType :: toRecord)
                                     .orElse(null);
    record.description     = description;
    record.system          = system;
    record.periodicity     = periodicity;
    record.releaseStage    = releaseStage;
    record.isActual        = isActual;
    record.tags            = tags;
    record.createdAt       = createdAt;
    record.lastModifiedAt  = lastModifiedAt;
    record.stagings        = stagings.stream()
                                     .map(Staging :: toRecord)
                                     .collect(Collectors.toList());
    record.businessClients = businessClients.stream()
                                            .map(Person :: toRecord)
                                            .collect(Collectors.toList());
    record.analysts        = analysts.stream()
                                     .map(Person :: toRecord)
                                     .collect(Collectors.toList());
    record.managers        = managers.stream()
                                     .map(Person :: toRecord)
                                     .collect(Collectors.toList());
    record.developers      = developers.stream()
                                       .map(Person :: toRecord)
                                       .collect(Collectors.toList());
    record.responsible     = responsible.stream()
                                        .map(Person :: toRecord)
                                        .collect(Collectors.toList());
    record.owners          = owners.stream()
                                   .map(Person :: toRecord)
                                   .collect(Collectors.toList());
    return record;
  }

  public DatamartRecord toDatamart() {

    final var record = new DatamartRecord();
    record.id              = id;
    record.name            = name;
    record.server          = server;
    record.dagId           = dagId;
    record.docType         = Optional.ofNullable(docType)
                                     .map(DocType :: toRecord)
                                     .orElse(null);
    record.storageTime     = storageTime;
    record.schedule        = schedule;
    record.path            = path;
    record.gitUrl          = gitUrl;
    record.confluenceUrl   = confluenceUrl;
    record.description     = description;
    record.system          = system;
    record.periodicity     = periodicity;
    record.releaseStage    = releaseStage;
    record.isActual        = isActual;
    record.tags            = tags;
    record.createdAt       = createdAt;
    record.lastModifiedAt  = lastModifiedAt;
    record.stagings        = stagings.stream()
                                     .map(Staging :: toRecord)
                                     .collect(Collectors.toList());
    record.businessClients = businessClients.stream()
                                            .map(Person :: toRecord)
                                            .collect(Collectors.toList());
    record.analysts        = analysts.stream()
                                     .map(Person :: toRecord)
                                     .collect(Collectors.toList());
    record.managers        = managers.stream()
                                     .map(Person :: toRecord)
                                     .collect(Collectors.toList());
    record.developers      = developers.stream()
                                       .map(Person :: toRecord)
                                       .collect(Collectors.toList());
    record.responsible     = responsible.stream()
                                        .map(Person :: toRecord)
                                        .collect(Collectors.toList());
    record.owners          = owners.stream()
                                   .map(Person :: toRecord)
                                   .collect(Collectors.toList());
    return record;
  }

  public TriggerRecord toTrigger() {

    final var record = new TriggerRecord();
    record.id              = id;
    record.name            = name;
    record.server          = server;
    record.dagId           = dagId;
    record.docType         = Optional.ofNullable(docType)
                                     .map(DocType :: toRecord)
                                     .orElse(null);
    record.triggerType     = triggerType;
    record.comment         = comment;
    record.gitUrl          = gitUrl;
    record.description     = description;
    record.system          = system;
    record.periodicity     = periodicity;
    record.releaseStage    = releaseStage;
    record.isActive        = isActive;
    record.isActual        = isActual;
    record.tags            = tags;
    record.createdAt       = createdAt;
    record.lastModifiedAt  = lastModifiedAt;
    record.stagings        = stagings.stream()
                                     .map(Staging :: toRecord)
                                     .collect(Collectors.toList());
    record.businessClients = businessClients.stream()
                                            .map(Person :: toRecord)
                                            .collect(Collectors.toList());
    record.analysts        = analysts.stream()
                                     .map(Person :: toRecord)
                                     .collect(Collectors.toList());
    record.managers        = managers.stream()
                                     .map(Person :: toRecord)
                                     .collect(Collectors.toList());
    record.developers      = developers.stream()
                                       .map(Person :: toRecord)
                                       .collect(Collectors.toList());
    record.responsible     = responsible.stream()
                                        .map(Person :: toRecord)
                                        .collect(Collectors.toList());
    record.owners          = owners.stream()
                                   .map(Person :: toRecord)
                                   .collect(Collectors.toList());
    return record;
  }

  public SourceRecord toSource() {

    final var record = new SourceRecord();
    record.id              = id;
    record.name            = name;
    record.server          = server;
    record.dagId           = dagId;
    record.docType         = Optional.ofNullable(docType)
                                     .map(DocType :: toRecord)
                                     .orElse(null);
    record.storageTime     = storageTime;
    record.comment         = comment;
    record.jiraTicket      = jiraTicket;
    record.startTime       = startTime;
    record.repeatQuantity  = repeatQuantity;
    record.description     = description;
    record.system          = system;
    record.periodicity     = periodicity;
    record.releaseStage    = releaseStage;
    record.isActual        = isActual;
    record.tags            = tags;
    record.createdAt       = createdAt;
    record.lastModifiedAt  = lastModifiedAt;
    record.stagings        = stagings.stream()
                                     .map(Staging :: toRecord)
                                     .collect(Collectors.toList());
    record.businessClients = businessClients.stream()
                                            .map(Person :: toRecord)
                                            .collect(Collectors.toList());
    record.analysts        = analysts.stream()
                                     .map(Person :: toRecord)
                                     .collect(Collectors.toList());
    record.managers        = managers.stream()
                                     .map(Person :: toRecord)
                                     .collect(Collectors.toList());
    record.developers      = developers.stream()
                                       .map(Person :: toRecord)
                                       .collect(Collectors.toList());
    record.responsible     = responsible.stream()
                                        .map(Person :: toRecord)
                                        .collect(Collectors.toList());
    record.owners          = owners.stream()
                                   .map(Person :: toRecord)
                                   .collect(Collectors.toList());
    return record;
  }

  public static DataCatalogRecord from(Hit<CatalogDocument> hit) {

    final var stagingNames = hit.innerHits().values().stream()
                                .map(InnerHitsResult :: hits)
                                .map(HitsMetadata :: hits)
                                .flatMap(Collection :: stream)
                                .map(Hit :: source)
                                .filter(Objects :: nonNull)
                                .map(JsonData :: toJson)
                                .map(JsonValue :: asJsonObject)
                                .map(f -> f.getString(Elastic.Fields.name))
                                .collect(Collectors.toSet());

    return Optional.ofNullable(hit.source())
                   .map(document -> {
                     if (!stagingNames.isEmpty()) {
                       document.stagings = document.stagings.stream()
                                                            .filter(s -> stagingNames.contains(s.name))
                                                            .collect(Collectors.toList());
                     }
                     return document.toRecord();
                   })
                   .orElse(null);
  }

}
