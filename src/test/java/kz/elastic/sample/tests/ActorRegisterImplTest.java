package kz.elastic.sample.tests;

import kz.elastic.sample.SampleApplicationTests;
import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.VoiceActor;
import kz.elastic.sample.register.ActorRegister;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ActorRegisterImplTest extends SampleApplicationTests {

  // region Autowired fields
  @Autowired
  private ActorRegister actorRegister;
  // endregion

  @Test
  void addActor() throws Exception {

    var actor = rndActor();

    //
    //
    actorRegister.addActor(actor);
    //
    //

    var getIndexRequest = new GetIndexRequest(ElasticSearch.actor());
    var getIndexResponse = ElasticSearch.client().indices().get(getIndexRequest, RequestOptions.DEFAULT);

    System.out.println("bb790m7jnn :: index settings:");
    printSettings(getIndexResponse.getSettings().get(ElasticSearch.actor()));

    var indexMatchMappings = getIndexResponse.getMappings().get(ElasticSearch.actor());
    System.out.println("oo3yh8xm0q :: index mappings:");
    printMappings(indexMatchMappings);

    var getRequest = new GetRequest(ElasticSearch.actor());
    getRequest.id(actor.id);
    var getResponse = ElasticSearch.client().get(getRequest, RequestOptions.DEFAULT);

    System.out.println("m14y4qtqz5 :: getResponse.source() = " + prettyJson(getResponse.getSourceAsString()));

    var source = getResponse.getSource();

    assertThat(source.get(VoiceActor.ES_SURNAME)).isEqualTo(actor.surname);
    assertThat(source.get(VoiceActor.ES_NAME)).isEqualTo(actor.name);

  }

}
