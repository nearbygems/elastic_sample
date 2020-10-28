package kz.elastic.sample;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.register.PersonRegister;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonRegisterTest extends SampleApplicationTests {

  // region Autowired fields
  @Autowired
  private PersonRegister personRegister;
  // endregion

  @Test
  void addPersonage() throws Exception {

    var person = rndPerson();

    //
    //
    personRegister.addPerson(person);
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
    getRequest.id(person.id);
    var getResponse = ElasticSearch.client().get(getRequest, RequestOptions.DEFAULT);

    System.out.println("KbKj71IqrJ :: getResponse.source() = " + prettyJson(getResponse.getSourceAsString()));
  }

}
