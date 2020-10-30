package kz.elastic.sample;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.Affiliation;
import kz.elastic.sample.register.AffiliationRegister;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class AffiliationRegisterImplTest extends SampleApplicationTests {

  // region Autowired fields
  @Autowired
  private AffiliationRegister affiliationRegister;
  // endregion

  @Test
  void addAffiliation() throws Exception {

    var affiliation = rndAffiliation();

    //
    //
    affiliationRegister.addAffiliation(affiliation);
    //
    //

    var getIndexRequest = new GetIndexRequest(ElasticSearch.affiliation());
    var getIndexResponse = ElasticSearch.client().indices().get(getIndexRequest, RequestOptions.DEFAULT);

    System.out.println("mrtti8lpzm :: index settings:");
    printSettings(getIndexResponse.getSettings().get(ElasticSearch.affiliation()));

    var indexMatchMappings = getIndexResponse.getMappings().get(ElasticSearch.affiliation());
    System.out.println("v39qwqv3wp :: index mappings:");
    printMappings(indexMatchMappings);

    var getRequest = new GetRequest(ElasticSearch.affiliation());
    getRequest.id(affiliation.id);
    var getResponse = ElasticSearch.client().get(getRequest, RequestOptions.DEFAULT);

    System.out.println("rmcvv7wotg :: getResponse.source() = " + prettyJson(getResponse.getSourceAsString()));
    var source = getResponse.getSource();

    assertThat(source.get(Affiliation.ES_NAME)).isEqualTo(affiliation.name);
    assertThat(source.get(Affiliation.ES_CAPTAIN)).isEqualTo(affiliation.captain);
    assertThat(source.get(Affiliation.ES_SHIP)).isEqualTo(affiliation.shipName);
    assertThat(source.get(Affiliation.ES_BOUNTY)).isEqualTo(affiliation.bounty);

    deleteIndex(ElasticSearch.affiliation());

  }

}
