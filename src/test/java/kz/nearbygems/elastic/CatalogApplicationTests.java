package kz.nearbygems.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import kz.nearbygems.elastic.models.elastic.CatalogDocument;
import kz.nearbygems.elastic.models.enums.DataType;
import kz.nearbygems.elastic.models.enums.Periodicity;
import kz.nearbygems.elastic.models.enums.ReleaseStage;
import kz.nearbygems.elastic.models.enums.SystemType;
import kz.nearbygems.elastic.models.web.DataCatalogRecord;
import kz.nearbygems.elastic.util.Constants;
import kz.nearbygems.elastic.util.Ids;
import kz.greetgo.util.RND;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.util.List;
import java.util.Objects;


@SpringBootTest
@ContextConfiguration(initializers = { CatalogApplicationTests.Initializer.class })
public class CatalogApplicationTests {

  @Autowired
  protected ElasticsearchClient client;

  @ClassRule
  public static ElasticsearchContainer container = new ElasticsearchContainer(
    DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.5.3"))
    .withEnv("discovery.type", "single-node")
    .withEnv("xpack.security.enabled", "false")
    .withCreateContainerCmdModifier(x -> Objects.requireNonNull(x.getHostConfig())
                                                .withMemory(6L * 1024 * 1024 * 1024));

  static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

      container.start();
      container.copyFileToContainer(MountableFile.forClasspathResource("hunspell"),
                                    "/usr/share/elasticsearch/config/hunspell");
      TestPropertyValues.of("elastic.port=" + container.getMappedPort(9200))
                        .applyTo(configurableApplicationContext.getEnvironment());

    }

  }

  protected void save(CatalogDocument document) {

    try {
      client.create(save -> save.index(Constants.DATA_CATALOG)
                                .id(document.id)
                                .document(document)
                                .refresh(Refresh.True));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected CatalogDocument load(String id) {

    try {
      return client.get(g -> g.index(Constants.DATA_CATALOG)
                              .id(id)
                              .refresh(true),
                        CatalogDocument.class)
                   .source();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  protected CatalogDocument rndDocument() {

    final var document = new CatalogDocument();
    document.id              = Ids.generate();
    document.name            = RND.str(4);
    document.server          = RND.str(4);
    document.comment         = RND.str(4);
    document.jiraTicket      = RND.str(4);
    document.storageTime     = RND.str(4);
    document.startTime       = RND.str(4);
    document.schedule        = RND.str(4);
    document.path            = RND.str(4);
    document.gitUrl          = RND.str(4);
    document.confluenceUrl   = RND.str(4);
    document.dagId           = RND.plusLong(100);
    document.repeatQuantity  = RND.plusLong(10);
    document.docType         = rndDocType();
    document.description     = RND.str(4);
    document.system          = RND.someEnum(SystemType.values());
    document.periodicity     = RND.someEnum(Periodicity.values());
    document.releaseStage    = RND.someEnum(ReleaseStage.values());
    document.isActive        = RND.bool();
    document.isActual        = RND.bool();
    document.tags            = new String[]{ RND.strEng(4), RND.strEng(4) };
    document.createdAt       = RND.dateYears(2020, 2021);
    document.lastModifiedAt  = RND.dateYears(2020, 2021);
    document.stagings        = List.of(rndStaging());
    document.businessClients = List.of(rndPerson(), rndPerson());
    document.analysts        = List.of(rndPerson(), rndPerson());
    document.managers        = List.of(rndPerson(), rndPerson());
    document.developers      = List.of(rndPerson(), rndPerson());
    document.responsible     = List.of(rndPerson(), rndPerson());
    document.owners          = List.of(rndPerson(), rndPerson());
    document.parents         = List.of(RND.str(4), RND.str(4));
    document.children        = List.of(RND.str(4), RND.str(4));
    return document;
  }

  protected DataCatalogRecord.StagingRecord rndStagingRecord() {

    final var staging = new DataCatalogRecord.StagingRecord();
    staging.name           = RND.strEng(4);
    staging.workId         = RND.plusInt(100);
    staging.description    = RND.str(4);
    staging.dataSourceName = RND.strEng(4);
    staging.outputs        = List.of(rndTransferRecord(), rndTransferRecord());
    staging.inputs         = List.of(rndTransferRecord(), rndTransferRecord());
    staging.columns        = List.of(rndColumnRecord(), rndColumnRecord());
    return staging;
  }

  protected DataCatalogRecord.StagingRecord.TransferRecord rndTransferRecord() {

    final var transfer = new DataCatalogRecord.StagingRecord.TransferRecord();
    transfer.system  = RND.strEng(4);
    transfer.storage = RND.strEng(4);
    transfer.host    = RND.strEng(4);
    transfer.port    = RND.str(4);
    return transfer;
  }

  protected DataCatalogRecord.StagingRecord.ColumnRecord rndColumnRecord() {

    final var transfer = new DataCatalogRecord.StagingRecord.ColumnRecord();
    transfer.name        = RND.strEng(4);
    transfer.description = RND.str(4);
    transfer.fieldType   = RND.strEng(4);
    transfer.orderIndex  = RND.plusLong(100);
    return transfer;
  }

  protected CatalogDocument.Staging rndStaging() {

    final var staging = new CatalogDocument.Staging();
    staging.name           = RND.strEng(4);
    staging.workId         = RND.plusInt(100);
    staging.description    = RND.str(4);
    staging.dataSourceName = RND.strEng(4);
    staging.outputs        = List.of(rndTransfer(), rndTransfer());
    staging.inputs         = List.of(rndTransfer(), rndTransfer());
    staging.columns        = List.of(rndColumn(), rndColumn());
    return staging;
  }

  protected CatalogDocument.Staging.Transfer rndTransfer() {

    final var transfer = new CatalogDocument.Staging.Transfer();
    transfer.system  = RND.strEng(4);
    transfer.storage = RND.strEng(4);
    transfer.host    = RND.strEng(4);
    transfer.port    = RND.str(4);
    return transfer;
  }

  protected CatalogDocument.Staging.Column rndColumn() {

    final var transfer = new CatalogDocument.Staging.Column();
    transfer.name        = RND.strEng(4);
    transfer.description = RND.str(4);
    transfer.fieldType   = RND.strEng(4);
    transfer.orderIndex  = RND.plusLong(100);
    return transfer;
  }

  protected DataCatalogRecord.DocTypeRecord rndDocTypeRecord() {

    final var doc = new DataCatalogRecord.DocTypeRecord();
    doc.type    = RND.someEnum(DataType.values());
    doc.subType = RND.strEng(4);
    return doc;
  }

  protected CatalogDocument.DocType rndDocType() {

    final var doc = new CatalogDocument.DocType();
    doc.type    = RND.someEnum(DataType.values());
    doc.subType = RND.strEng(4);
    return doc;
  }

  protected DataCatalogRecord.PersonRecord rndPersonRecord() {

    final var person = new DataCatalogRecord.PersonRecord();
    person.email     = RND.strEng(4);
    person.firstName = RND.str(4);
    person.lastName  = RND.str(4);
    return person;
  }

  protected CatalogDocument.Person rndPerson() {

    final var person = new CatalogDocument.Person();
    person.email     = RND.strEng(4);
    person.firstName = RND.str(4);
    person.lastName  = RND.str(4);
    return person;
  }

}
