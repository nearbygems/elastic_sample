package kz.nearbygems.elastic.models.web;

import kz.nearbygems.elastic.models.elastic.CatalogDocument;
import kz.nearbygems.elastic.models.enums.DataType;
import kz.nearbygems.elastic.models.enums.Periodicity;
import kz.nearbygems.elastic.models.enums.ReleaseStage;
import kz.nearbygems.elastic.models.enums.SystemType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@ToString
public class DataCatalogRecord {

  public String              id;
  public String              name;
  public long                dagId;
  public String              server;
  public String              description;
  public DocTypeRecord docType;
  public SystemType    system;
  public Periodicity   periodicity;
  public ReleaseStage  releaseStage;
  public String[]      tags;
  public Date                createdAt;
  public Date                lastModifiedAt;
  public boolean             isActual;
  public List<StagingRecord> stagings        = new ArrayList<>();
  public List<PersonRecord>  businessClients = new ArrayList<>();
  public List<PersonRecord>  analysts        = new ArrayList<>();
  public List<PersonRecord>  managers        = new ArrayList<>();
  public List<PersonRecord>  developers      = new ArrayList<>();
  public List<PersonRecord>  responsible     = new ArrayList<>();
  public List<PersonRecord>  owners          = new ArrayList<>();

  public static class StagingRecord {

    public String name;
    public long   workId;
    public String description;
    public String dataSourceName;

    public List<TransferRecord> outputs = new ArrayList<>();
    public List<TransferRecord> inputs  = new ArrayList<>();
    public List<ColumnRecord>   columns = new ArrayList<>();

    public CatalogDocument.Staging toDocument() {

      final var document = new CatalogDocument.Staging();
      document.name           = name;
      document.workId         = workId;
      document.description    = description;
      document.dataSourceName = dataSourceName;
      document.outputs        = outputs.stream().map(TransferRecord :: toDocument).collect(Collectors.toList());
      document.inputs         = inputs.stream().map(TransferRecord :: toDocument).collect(Collectors.toList());
      document.columns        = columns.stream().map(ColumnRecord :: toDocument).collect(Collectors.toList());
      return document;
    }


    @EqualsAndHashCode
    public static class TransferRecord {

      public String system;
      public String storage;
      public String host;
      public String port;

      public CatalogDocument.Staging.Transfer toDocument() {

        final var document = new CatalogDocument.Staging.Transfer();
        document.system  = system;
        document.storage = storage;
        document.host    = host;
        document.port    = port;
        return document;
      }

    }

    @ToString
    @EqualsAndHashCode
    public static class ColumnRecord {

      public String name;
      public String description;
      public String fieldType;
      public long   orderIndex;

      public CatalogDocument.Staging.Column toDocument() {

        final var document = new CatalogDocument.Staging.Column();
        document.name        = name;
        document.description = description;
        document.fieldType   = fieldType;
        document.orderIndex  = orderIndex;
        return document;
      }

    }

  }

  @EqualsAndHashCode
  public static class DocTypeRecord {

    public DataType type;
    public String   subType;

    public CatalogDocument.DocType toDocument() {

      final var document = new CatalogDocument.DocType();
      document.type    = type;
      document.subType = subType;
      return document;
    }

    public static DocTypeRecord from(ComputingTypeRecord record) {

      final var docType = new DocTypeRecord();
      docType.type    = record.type;
      docType.subType = record.name;
      return docType;
    }

  }

  @EqualsAndHashCode
  public static class PersonRecord {

    public String email;
    public String lastName;
    public String firstName;

    public CatalogDocument.Person toDocument() {

      final var document = new CatalogDocument.Person();
      document.email     = email;
      document.lastName  = lastName;
      document.firstName = firstName;
      return document;
    }

  }

}
