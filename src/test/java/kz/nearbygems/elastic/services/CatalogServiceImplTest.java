package kz.nearbygems.elastic.services;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import kz.nearbygems.elastic.CatalogApplicationTests;
import kz.nearbygems.elastic.models.elastic.CatalogDocument;
import kz.nearbygems.elastic.models.enums.Periodicity;
import kz.nearbygems.elastic.models.enums.ReleaseStage;
import kz.nearbygems.elastic.models.enums.SystemType;
import kz.nearbygems.elastic.models.web.Filter;
import kz.nearbygems.elastic.models.web.Page;
import kz.nearbygems.elastic.models.web.SearchRequest;
import kz.nearbygems.elastic.util.Constants;
import kz.nearbygems.elastic.util.Elastic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class CatalogServiceImplTest extends CatalogApplicationTests {

  @Autowired
  private CatalogService service;

  @Test
  void searchMatch() {

    final var search = "датамарт";

    final var documentName = rndDocument();
    documentName.name = "datamart";
    save(documentName);

    final var documentServer = rndDocument();
    documentServer.server = "datamart";
    save(documentServer);

    final var documentComment = rndDocument();
    documentComment.comment = "datamart";
    save(documentComment);

    final var documentDescription = rndDocument();
    documentDescription.description = "datamart";
    save(documentDescription);

    final var documentNo = rndDocument();
    save(documentNo);

    final var req = SearchRequest.search(search);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(4);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentName.id);
    assertThat(ids).contains(documentServer.id);
    assertThat(ids).contains(documentComment.id);
    assertThat(ids).contains(documentDescription.id);
  }

  @Test
  void searchPhrase() {

    final var search = "data";

    final var documentName = rndDocument();
    documentName.name = "datamart";
    save(documentName);

    final var documentServer = rndDocument();
    documentServer.server = "datamart";
    save(documentServer);

    final var documentComment = rndDocument();
    documentComment.comment = "datamart";
    save(documentComment);

    final var documentDescription = rndDocument();
    documentDescription.description = "datamart";
    save(documentDescription);

    final var documentNo = rndDocument();
    save(documentNo);

    final var req = SearchRequest.search(search);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(4);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentName.id);
    assertThat(ids).contains(documentServer.id);
    assertThat(ids).contains(documentComment.id);
    assertThat(ids).contains(documentDescription.id);
  }

  @Test
  void searchPerson() {

    final var search   = "берг";
    final var searched = "берген";

    final var documentClientEmail = rndDocument();
    documentClientEmail.businessClients.stream().findAny().ifPresent(person -> person.email = searched);
    save(documentClientEmail);

    final var documentClientLastName = rndDocument();
    documentClientLastName.businessClients.stream().findAny().ifPresent(person -> person.lastName = searched);
    save(documentClientLastName);

    final var documentClientFirstName = rndDocument();
    documentClientFirstName.businessClients.stream().findAny().ifPresent(person -> person.firstName = searched);
    save(documentClientFirstName);

    final var documentAnalystEmail = rndDocument();
    documentAnalystEmail.analysts.stream().findAny().ifPresent(person -> person.email = searched);
    save(documentAnalystEmail);

    final var documentAnalystLastName = rndDocument();
    documentAnalystLastName.analysts.stream().findAny().ifPresent(person -> person.lastName = searched);
    save(documentAnalystLastName);

    final var documentAnalystFirstName = rndDocument();
    documentAnalystFirstName.analysts.stream().findAny().ifPresent(person -> person.firstName = searched);
    save(documentAnalystFirstName);

    final var documentManagerEmail = rndDocument();
    documentManagerEmail.managers.stream().findAny().ifPresent(person -> person.email = searched);
    save(documentManagerEmail);

    final var documentManagerLastName = rndDocument();
    documentManagerLastName.managers.stream().findAny().ifPresent(person -> person.lastName = searched);
    save(documentManagerLastName);

    final var documentManagerFirstName = rndDocument();
    documentManagerFirstName.managers.stream().findAny().ifPresent(person -> person.firstName = searched);
    save(documentManagerFirstName);

    final var documentDeveloperEmail = rndDocument();
    documentDeveloperEmail.developers.stream().findAny().ifPresent(person -> person.email = searched);
    save(documentDeveloperEmail);

    final var documentDeveloperLastName = rndDocument();
    documentDeveloperLastName.developers.stream().findAny().ifPresent(person -> person.lastName = searched);
    save(documentDeveloperLastName);

    final var documentDeveloperFirstName = rndDocument();
    documentDeveloperFirstName.developers.stream().findAny().ifPresent(person -> person.firstName = searched);
    save(documentDeveloperFirstName);

    final var documentResponsibleEmail = rndDocument();
    documentResponsibleEmail.responsible.stream().findAny().ifPresent(person -> person.email = searched);
    save(documentResponsibleEmail);

    final var documentResponsibleLastName = rndDocument();
    documentResponsibleLastName.responsible.stream().findAny().ifPresent(person -> person.lastName = searched);
    save(documentResponsibleLastName);

    final var documentResponsibleFirstName = rndDocument();
    documentResponsibleFirstName.responsible.stream().findAny().ifPresent(person -> person.firstName = searched);
    save(documentResponsibleFirstName);

    final var documentOwnerEmail = rndDocument();
    documentOwnerEmail.owners.stream().findAny().ifPresent(person -> person.email = searched);
    save(documentOwnerEmail);

    final var documentOwnerLastName = rndDocument();
    documentOwnerLastName.owners.stream().findAny().ifPresent(person -> person.lastName = searched);
    save(documentOwnerLastName);

    final var documentOwnerFirstName = rndDocument();
    documentOwnerFirstName.owners.stream().findAny().ifPresent(person -> person.firstName = searched);
    save(documentOwnerFirstName);

    final var documentNo = rndDocument();
    save(documentNo);

    final var req = SearchRequest.search(search);
    req.page = Page.of(0, 20);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(18);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentClientEmail.id);
    assertThat(ids).contains(documentClientLastName.id);
    assertThat(ids).contains(documentClientFirstName.id);
    assertThat(ids).contains(documentAnalystEmail.id);
    assertThat(ids).contains(documentAnalystLastName.id);
    assertThat(ids).contains(documentAnalystFirstName.id);
    assertThat(ids).contains(documentManagerEmail.id);
    assertThat(ids).contains(documentManagerLastName.id);
    assertThat(ids).contains(documentManagerFirstName.id);
    assertThat(ids).contains(documentDeveloperEmail.id);
    assertThat(ids).contains(documentDeveloperLastName.id);
    assertThat(ids).contains(documentDeveloperFirstName.id);
    assertThat(ids).contains(documentResponsibleEmail.id);
    assertThat(ids).contains(documentResponsibleLastName.id);
    assertThat(ids).contains(documentResponsibleFirstName.id);
    assertThat(ids).contains(documentOwnerEmail.id);
    assertThat(ids).contains(documentOwnerLastName.id);
    assertThat(ids).contains(documentOwnerFirstName.id);
  }

  @Test
  void searchStaging() {

    final var search   = "берг";
    final var searched = "берген";

    final var documentName = rndDocument();
    documentName.stagings.stream().findAny().ifPresent(staging -> staging.name = searched);
    save(documentName);

    final var documentDataSourceName = rndDocument();
    documentDataSourceName.stagings.stream().findAny().ifPresent(staging -> staging.dataSourceName = searched);
    save(documentDataSourceName);

    final var documentDescription = rndDocument();
    documentDescription.stagings.stream().findAny().ifPresent(staging -> staging.description = searched);
    save(documentDescription);

    final var documentNo = rndDocument();
    save(documentNo);

    final var req = SearchRequest.search(search);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(3);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentName.id);
    assertThat(ids).contains(documentDataSourceName.id);
    assertThat(ids).contains(documentDescription.id);
  }

  @Test
  void searchColumns() {

    final var search   = "берг";
    final var searched = "берген";

    final var documentName = rndDocument();
    documentName.stagings.stream()
                         .findAny()
                         .flatMap(staging -> staging.columns.stream().findAny())
                         .ifPresent(column -> column.name = searched);
    save(documentName);

    final var documentDescription = rndDocument();
    documentDescription.stagings.stream()
                                .findAny()
                                .flatMap(staging -> staging.columns.stream().findAny())
                                .ifPresent(column -> column.description = searched);
    save(documentDescription);

    final var documentNo = rndDocument();
    save(documentNo);

    final var req = SearchRequest.search(search);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(2);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentName.id);
    assertThat(ids).contains(documentDescription.id);
  }

  @Test
  void searchTransfers() {

    final var search   = "берг";
    final var searched = "берген";

    final var documentOutputSystem = rndDocument();
    documentOutputSystem.stagings.stream()
                                 .findAny()
                                 .flatMap(staging -> staging.outputs.stream().findAny())
                                 .ifPresent(column -> column.system = searched);
    save(documentOutputSystem);

    final var documentOutputStorage = rndDocument();
    documentOutputStorage.stagings.stream()
                                  .findAny()
                                  .flatMap(staging -> staging.outputs.stream().findAny())
                                  .ifPresent(column -> column.storage = searched);
    save(documentOutputStorage);

    final var documentOutputHost = rndDocument();
    documentOutputHost.stagings.stream()
                               .findAny()
                               .flatMap(staging -> staging.outputs.stream().findAny())
                               .ifPresent(column -> column.host = searched);
    save(documentOutputHost);

    final var documentInputSystem = rndDocument();
    documentInputSystem.stagings.stream()
                                .findAny()
                                .flatMap(staging -> staging.inputs.stream().findAny())
                                .ifPresent(column -> column.system = searched);
    save(documentInputSystem);

    final var documentInputStorage = rndDocument();
    documentInputStorage.stagings.stream()
                                 .findAny()
                                 .flatMap(staging -> staging.inputs.stream().findAny())
                                 .ifPresent(column -> column.storage = searched);
    save(documentInputStorage);

    final var documentInputHost = rndDocument();
    documentInputHost.stagings.stream()
                              .findAny()
                              .flatMap(staging -> staging.inputs.stream().findAny())
                              .ifPresent(column -> column.host = searched);
    save(documentInputHost);

    final var documentNo = rndDocument();
    save(documentNo);

    final var req = SearchRequest.search(search);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(6);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOutputSystem.id);
    assertThat(ids).contains(documentOutputStorage.id);
    assertThat(ids).contains(documentOutputHost.id);
    assertThat(ids).contains(documentInputSystem.id);
    assertThat(ids).contains(documentInputStorage.id);
    assertThat(ids).contains(documentInputHost.id);
  }

  @Test
  void filterBoolean() {

    final var searched = "true";

    final var documentOutputIsActive = rndDocument();
    documentOutputIsActive.isActive = true;
    documentOutputIsActive.isActual = true;
    save(documentOutputIsActive);

    final var documentOutputIsActual = rndDocument();
    documentOutputIsActual.isActive = true;
    documentOutputIsActual.isActual = true;
    save(documentOutputIsActual);

    final var documentActiveNo = rndDocument();
    documentActiveNo.isActive = false;
    documentActiveNo.isActual = true;
    save(documentActiveNo);

    final var documentActualNo = rndDocument();
    documentActualNo.isActual = false;
    documentActualNo.isActive = true;
    save(documentActualNo);

    final var active = new Filter();
    active.field = Elastic.Fields.isActive;
    active.ids   = List.of(searched);

    final var actual = new Filter();
    actual.field = Elastic.Fields.isActual;
    actual.ids   = List.of(searched);

    final var req = new SearchRequest();
    req.filters = List.of(active, actual);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(2);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOutputIsActive.id);
    assertThat(ids).contains(documentOutputIsActual.id);
  }

  @Test
  void filterSystem() {

    final var searched = SystemType.AIRFLOW;

    final var documentOk = rndDocument();
    documentOk.system = searched;
    save(documentOk);

    final var documentNo = rndDocument();
    documentNo.system = SystemType.DMC;
    save(documentNo);

    final var system = new Filter();
    system.field = Elastic.Fields.system;
    system.ids   = List.of(searched.name());

    final var req = new SearchRequest();
    req.filters = List.of(system);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(1);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOk.id);
  }

  @Test
  void filterPeriodicity() {

    final var searched = Periodicity.DAILY;

    final var documentOk = rndDocument();
    documentOk.periodicity = searched;
    save(documentOk);

    final var documentNo = rndDocument();
    documentNo.periodicity = Periodicity.NONE;
    save(documentNo);

    final var periodicity = new Filter();
    periodicity.field = Elastic.Fields.periodicity;
    periodicity.ids   = List.of(searched.name());

    final var req = new SearchRequest();
    req.filters = List.of(periodicity);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(1);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOk.id);
  }

  @Test
  void filterReleaseStage() {

    final var searched = ReleaseStage.DEV;

    final var documentOk = rndDocument();
    documentOk.releaseStage = searched;
    save(documentOk);

    final var documentNo = rndDocument();
    documentNo.releaseStage = ReleaseStage.PROD;
    save(documentNo);

    final var releaseStage = new Filter();
    releaseStage.field = Elastic.Fields.releaseStage;
    releaseStage.ids   = List.of(searched.name());

    final var req = new SearchRequest();
    req.filters = List.of(releaseStage);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(1);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOk.id);
  }

  @Test
  void filterTags() {

    final var searched = "tag";

    final var documentOk = rndDocument();
    documentOk.tags = new String[]{ searched };
    save(documentOk);

    final var documentNo = rndDocument();
    save(documentNo);

    final var tags = new Filter();
    tags.field = Elastic.Fields.tags;
    tags.ids   = List.of(searched);

    final var req = new SearchRequest();
    req.filters = List.of(tags);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(1);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOk.id);
  }

  @Test
  void filterJiraTicket() {

    final var searched = "ticket";

    final var documentOk = rndDocument();
    documentOk.jiraTicket = searched;
    save(documentOk);

    final var documentNo = rndDocument();
    save(documentNo);

    final var jiraTicket = new Filter();
    jiraTicket.field = Elastic.Fields.jiraTicket;
    jiraTicket.ids   = List.of(searched);

    final var req = new SearchRequest();
    req.filters = List.of(jiraTicket);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(1);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOk.id);
  }

  @Test
  void filterStorageTime() {

    final var searched = "time";

    final var documentOk = rndDocument();
    documentOk.storageTime = searched;
    save(documentOk);

    final var documentNo = rndDocument();
    save(documentNo);

    final var storageTime = new Filter();
    storageTime.field = Elastic.Fields.storageTime;
    storageTime.ids   = List.of(searched);

    final var req = new SearchRequest();
    req.filters = List.of(storageTime);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(1);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOk.id);
  }

  @Test
  void filterStartTime() {

    final var searched = "time";

    final var documentOk = rndDocument();
    documentOk.startTime = searched;
    save(documentOk);

    final var documentNo = rndDocument();
    save(documentNo);

    final var startTime = new Filter();
    startTime.field = Elastic.Fields.startTime;
    startTime.ids   = List.of(searched);

    final var req = new SearchRequest();
    req.filters = List.of(startTime);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(1);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOk.id);
  }

  @Test
  void filterSchedule() {

    final var searched = "time";

    final var documentOk = rndDocument();
    documentOk.schedule = searched;
    save(documentOk);

    final var documentNo = rndDocument();
    save(documentNo);

    final var schedule = new Filter();
    schedule.field = Elastic.Fields.schedule;
    schedule.ids   = List.of(searched);

    final var req = new SearchRequest();
    req.filters = List.of(schedule);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(1);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOk.id);
  }

  @Test
  void filterPath() {

    final var searched = "path";

    final var documentOk = rndDocument();
    documentOk.path = searched;
    save(documentOk);

    final var documentNo = rndDocument();
    save(documentNo);

    final var path = new Filter();
    path.field = Elastic.Fields.path;
    path.ids   = List.of(searched);

    final var req = new SearchRequest();
    req.filters = List.of(path);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(1);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOk.id);
  }

  @Test
  void filterCreatedAt() {

    final var searched = LocalDate.now();

    final var from = Date.from(searched.minusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());
    final var to   = Date.from(searched.plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());

    final var documentOk = rndDocument();
    documentOk.createdAt = Date.from(searched.atStartOfDay(ZoneId.systemDefault()).toInstant());
    save(documentOk);

    final var documentNo = rndDocument();
    documentNo.createdAt = Date.from(searched.minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
    save(documentNo);

    final var createdAt = new Filter();
    createdAt.field    = Elastic.Fields.createdAt;
    createdAt.dateFrom = from;
    createdAt.dateTo   = to;

    final var req = new SearchRequest();
    req.filters = List.of(createdAt);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(1);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOk.id);
  }

  @Test
  void filterLastModifiedAt() {

    final var searched = LocalDate.now();

    final var from = Date.from(searched.minusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());
    final var to   = Date.from(searched.plusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());

    final var documentOk = rndDocument();
    documentOk.lastModifiedAt = Date.from(searched.atStartOfDay(ZoneId.systemDefault()).toInstant());
    save(documentOk);

    final var documentNo = rndDocument();
    documentNo.lastModifiedAt = Date.from(searched.minusDays(3).atStartOfDay(ZoneId.systemDefault()).toInstant());
    save(documentNo);

    final var lastModifiedAt = new Filter();
    lastModifiedAt.field    = Elastic.Fields.lastModifiedAt;
    lastModifiedAt.dateFrom = from;
    lastModifiedAt.dateTo   = to;

    final var req = new SearchRequest();
    req.filters = List.of(lastModifiedAt);

    //
    final var resp = service.search(req);
    //

    assertThat(resp).isNotNull();

    final var records = resp.records;

    assertThat(records).hasSize(1);

    final var ids = records.stream().map(r -> r.id).collect(Collectors.toSet());

    assertThat(ids).contains(documentOk.id);
  }

  @Test
  void count() {

    final var docType = rndDocType();

    final var documentOk1 = rndDocument();
    documentOk1.docType = docType;
    save(documentOk1);

    final var documentOk2 = rndDocument();
    documentOk2.docType = docType;
    save(documentOk2);

    final var documentNo = rndDocument();
    save(documentNo);

    //
    final var count = service.count(docType.toRecord());
    //

    assertThat(count).isEqualTo(2);
  }

  @Test
  void delete() throws IOException {

    final var document = new CatalogDocument();
    document.id = "id";

    save(document);

    //
    service.delete(document.id);
    //

    final var exists = client.exists(ex -> ex.index(Constants.DATA_CATALOG).id(document.id)).value();

    assertThat(exists).isFalse();
  }

  @Test
  void loadFields() {

    final var document = rndDocument();

    final var staging = document.stagings.get(0);

    save(document);

    //
    final var fields = service.loadFields(document.id, staging.name);
    //

    assertThat(fields).isNotEmpty();
    assertThat(fields).hasSameElementsAs(staging.toRecord().columns);
  }

  @BeforeEach
  public void clean() {

    try {
      client.deleteByQuery(d -> d.index(Constants.DATA_CATALOG).query(Query.of(f -> f.matchAll(a -> a))).refresh(true));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
