package kz.elastic.sample.tests;

import kz.elastic.sample.SampleApplicationTests;
import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.DevilFruit;
import kz.elastic.sample.register.FruitRegister;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FruitRegisterImplTest extends SampleApplicationTests {

  // region Autowired fields
  @Autowired
  private FruitRegister fruitRegister;
  // endregion

  @Test
  void addFruit() throws IOException {

    var fruit = rndFruit();

    //
    //
    fruitRegister.addFruit(fruit);
    //
    //

    var getIndexRequest = new GetIndexRequest(ElasticSearch.fruit());
    var getIndexResponse = ElasticSearch.client().indices().get(getIndexRequest, RequestOptions.DEFAULT);

    System.out.println("fjtqenmx9m :: index settings:");
    printSettings(getIndexResponse.getSettings().get(ElasticSearch.fruit()));

    var indexMatchMappings = getIndexResponse.getMappings().get(ElasticSearch.fruit());
    System.out.println("xspi8gfivf :: index mappings:");
    printMappings(indexMatchMappings);

    var getRequest = new GetRequest(ElasticSearch.fruit());
    getRequest.id(fruit.id);
    var getResponse = ElasticSearch.client().get(getRequest, RequestOptions.DEFAULT);

    System.out.println("d83vecdtup :: getResponse.source() = " + prettyJson(getResponse.getSourceAsString()));
    var source = getResponse.getSource();

    assertThat(source.get(DevilFruit.ES_NAME)).isEqualTo(fruit.name);
    assertThat(source.get(DevilFruit.ES_MEANING)).isEqualTo(fruit.meaning);
    assertThat(source.get(DevilFruit.ES_TYPE)).isEqualTo(fruit.type.toString());

  }

}
