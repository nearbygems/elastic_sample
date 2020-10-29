package kz.elastic.sample;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.register.FruitRegister;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FruitRegisterImplTest extends SampleApplicationTests {

  // region Autowired fields
  @Autowired
  private FruitRegister fruitRegister;
  // endregion

  @Test
  void addPerson() throws Exception {

    var fruit = rndFruit();

    //
    //
    fruitRegister.addFruit(fruit);
    //
    //

    var getIndexRequest = new GetIndexRequest(ElasticSearch.person());
    var getIndexResponse = ElasticSearch.client().indices().get(getIndexRequest, RequestOptions.DEFAULT);

    System.out.println("\n8uj6dX1D5e :: index settings:");
    printSettings(getIndexResponse.getSettings().get(ElasticSearch.person()));

    var indexMatchMappings = getIndexResponse.getMappings().get(ElasticSearch.person());
    System.out.println("\n9VtYCQwRxg :: index mappings:");
    printMappings(indexMatchMappings);

    var getRequest = new GetRequest(ElasticSearch.person());
    getRequest.id(fruit.id);
    var getResponse = ElasticSearch.client().get(getRequest, RequestOptions.DEFAULT);

    System.out.println("KbKj71IqrJ :: getResponse.source() = " + prettyJson(getResponse.getSourceAsString()));

  }

}
