package kz.nearbygems.elastic.services;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import kz.nearbygems.elastic.CatalogApplicationTests;
import kz.nearbygems.elastic.models.elastic.CatalogDocument;
import kz.nearbygems.elastic.models.enums.Periodicity;
import kz.nearbygems.elastic.models.enums.ReleaseStage;
import kz.nearbygems.elastic.models.enums.SystemType;
import kz.nearbygems.elastic.models.web.DataCatalogRecord;
import kz.nearbygems.elastic.models.web.SourceRecord;
import kz.nearbygems.elastic.util.Constants;
import kz.nearbygems.elastic.util.Ids;
import kz.greetgo.util.RND;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class SourceServiceImplTest extends CatalogApplicationTests {

  @Autowired
  private SourceService service;

  @Test
  void loadById() {

    final var document = rndDocument();

    save(document);

    //
    final var record = service.loadById(document.id);
    //

    assertThat(record).isNotNull();
    assertThat(record.id).isEqualTo(document.id);
    assertThat(record.name).isEqualTo(document.name);
    assertThat(record.server).isEqualTo(document.server);
    assertThat(record.storageTime).isEqualTo(document.storageTime);
    assertThat(record.startTime).isEqualTo(document.startTime);
    assertThat(record.comment).isEqualTo(document.comment);
    assertThat(record.jiraTicket).isEqualTo(document.jiraTicket);
    assertThat(record.comment).isEqualTo(document.comment);
    assertThat(record.repeatQuantity).isEqualTo(document.repeatQuantity);
    assertThat(record.dagId).isEqualTo(document.dagId);
    assertThat(record.docType.toDocument()).isEqualTo(document.docType);
    assertThat(record.description).isEqualTo(document.description);
    assertThat(record.system).isEqualTo(document.system);
    assertThat(record.periodicity).isEqualTo(document.periodicity);
    assertThat(record.releaseStage).isEqualTo(document.releaseStage);
    assertThat(record.isActual).isEqualTo(document.isActual);
    assertThat(record.tags).hasSameElementsAs(Arrays.asList(document.tags));
    assertThat(record.createdAt).isEqualTo(document.createdAt);
    assertThat(record.lastModifiedAt).isEqualTo(document.lastModifiedAt);
    assertThat(record.businessClients.stream()
                                     .map(DataCatalogRecord.PersonRecord :: toDocument)
                                     .collect(Collectors.toList())).hasSameElementsAs(document.businessClients);
    assertThat(record.analysts.stream()
                              .map(DataCatalogRecord.PersonRecord :: toDocument)
                              .collect(Collectors.toList())).hasSameElementsAs(document.analysts);
    assertThat(record.managers.stream()
                              .map(DataCatalogRecord.PersonRecord :: toDocument)
                              .collect(Collectors.toList())).hasSameElementsAs(document.managers);
    assertThat(record.developers.stream()
                                .map(DataCatalogRecord.PersonRecord :: toDocument)
                                .collect(Collectors.toList())).hasSameElementsAs(document.developers);
    assertThat(record.responsible.stream()
                                 .map(DataCatalogRecord.PersonRecord :: toDocument)
                                 .collect(Collectors.toList())).hasSameElementsAs(document.responsible);
    assertThat(record.owners.stream()
                            .map(DataCatalogRecord.PersonRecord :: toDocument)
                            .collect(Collectors.toList())).hasSameElementsAs(document.owners);

    assertThat(record.stagings).hasSize(1);

    final var stagingRecord = record.stagings.get(0).toDocument();
    final var staging       = document.stagings.get(0);

    assertThat(stagingRecord.name).isEqualTo(staging.name);
    assertThat(stagingRecord.workId).isEqualTo(staging.workId);
    assertThat(stagingRecord.description).isEqualTo(staging.description);
    assertThat(stagingRecord.dataSourceName).isEqualTo(staging.dataSourceName);
    assertThat(stagingRecord.outputs).hasSameElementsAs(staging.outputs);
    assertThat(stagingRecord.inputs).hasSameElementsAs(staging.inputs);
    assertThat(stagingRecord.columns).hasSameElementsAs(staging.columns);
  }

  @Test
  void save() {

    final var record = rndSourceRecord();

    //
    service.save(record);
    //

    final var document = load(record.id);

    assertThat(document).isNotNull();
    assertThat(document.id).isEqualTo(record.id);
    assertThat(document.name).isEqualTo(record.name);
    assertThat(document.server).isEqualTo(record.server);
    assertThat(document.storageTime).isEqualTo(record.storageTime);
    assertThat(document.comment).isEqualTo(record.comment);
    assertThat(document.startTime).isEqualTo(record.startTime);
    assertThat(document.jiraTicket).isEqualTo(record.jiraTicket);
    assertThat(document.repeatQuantity).isEqualTo(record.repeatQuantity);
    assertThat(document.dagId).isEqualTo(record.dagId);
    assertThat(document.docType.toRecord()).isEqualTo(record.docType);
    assertThat(document.description).isEqualTo(record.description);
    assertThat(document.system).isEqualTo(record.system);
    assertThat(document.periodicity).isEqualTo(record.periodicity);
    assertThat(document.releaseStage).isEqualTo(record.releaseStage);
    assertThat(document.isActual).isEqualTo(record.isActual);
    assertThat(document.tags).hasSameElementsAs(Arrays.asList(record.tags));
    assertThat(document.createdAt).isEqualTo(record.createdAt);
    assertThat(document.lastModifiedAt).isEqualTo(record.lastModifiedAt);
    assertThat(document.businessClients.stream()
                                       .map(CatalogDocument.Person :: toRecord)
                                       .collect(Collectors.toList())).hasSameElementsAs(record.businessClients);
    assertThat(document.analysts.stream()
                                .map(CatalogDocument.Person :: toRecord)
                                .collect(Collectors.toList())).hasSameElementsAs(record.analysts);
    assertThat(document.managers.stream()
                                .map(CatalogDocument.Person :: toRecord)
                                .collect(Collectors.toList())).hasSameElementsAs(record.managers);
    assertThat(document.developers.stream()
                                  .map(CatalogDocument.Person :: toRecord)
                                  .collect(Collectors.toList())).hasSameElementsAs(record.developers);
    assertThat(document.responsible.stream()
                                   .map(CatalogDocument.Person :: toRecord)
                                   .collect(Collectors.toList())).hasSameElementsAs(record.responsible);
    assertThat(document.owners.stream()
                              .map(CatalogDocument.Person :: toRecord)
                              .collect(Collectors.toList())).hasSameElementsAs(record.owners);

    assertThat(document.stagings).hasSize(1);

    final var stagingRecord = document.stagings.get(0).toRecord();
    final var staging       = record.stagings.get(0);

    assertThat(stagingRecord.name).isEqualTo(staging.name);
    assertThat(stagingRecord.workId).isEqualTo(staging.workId);
    assertThat(stagingRecord.description).isEqualTo(staging.description);
    assertThat(stagingRecord.dataSourceName).isEqualTo(staging.dataSourceName);
    assertThat(stagingRecord.outputs).hasSameElementsAs(staging.outputs);
    assertThat(stagingRecord.inputs).hasSameElementsAs(staging.inputs);
    assertThat(stagingRecord.columns).hasSameElementsAs(staging.columns);
  }

  @BeforeEach
  public void clean() {

    try {
      client.deleteByQuery(d -> d.index(Constants.DATA_CATALOG)
                                 .query(Query.of(f -> f.matchAll(a -> a)))
                                 .refresh(true));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private SourceRecord rndSourceRecord() {

    final var record = new SourceRecord();
    record.id              = Ids.generate();
    record.name            = RND.str(4);
    record.server          = RND.str(4);
    record.storageTime     = RND.str(4);
    record.comment         = RND.str(4);
    record.jiraTicket      = RND.str(4);
    record.startTime       = RND.str(4);
    record.repeatQuantity  = RND.plusLong(100);
    record.comment         = RND.str(4);
    record.dagId           = RND.plusLong(100);
    record.docType         = rndDocTypeRecord();
    record.description     = RND.str(4);
    record.system          = RND.someEnum(SystemType.values());
    record.periodicity     = RND.someEnum(Periodicity.values());
    record.releaseStage    = RND.someEnum(ReleaseStage.values());
    record.isActual        = RND.bool();
    record.tags            = new String[]{ RND.strEng(4), RND.strEng(4) };
    record.createdAt       = RND.dateYears(2020, 2021);
    record.lastModifiedAt  = RND.dateYears(2020, 2021);
    record.stagings        = List.of(rndStagingRecord());
    record.businessClients = List.of(rndPersonRecord(), rndPersonRecord());
    record.analysts        = List.of(rndPersonRecord(), rndPersonRecord());
    record.managers        = List.of(rndPersonRecord(), rndPersonRecord());
    record.developers      = List.of(rndPersonRecord(), rndPersonRecord());
    record.responsible     = List.of(rndPersonRecord(), rndPersonRecord());
    record.owners          = List.of(rndPersonRecord(), rndPersonRecord());
    return record;
  }

}
