package kz.elastic.sample.tests;

import kz.elastic.sample.SampleApplicationTests;
import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.VoiceActor;
import kz.elastic.sample.register.ActorRegister;
import kz.elastic.sample.util.Ids;
import kz.greetgo.util.RND;
import lombok.SneakyThrows;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ActorRegisterImplTest extends SampleApplicationTests {

  // region Autowired fields
  @Autowired
  private ActorRegister actorRegister;
  // endregion

  @Test
  @SneakyThrows
  void addActor() {

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

  @Test
  @SneakyThrows
  void searchActor() {

    var search = RND.str(5);

    var actorOk1 = new VoiceActor();
    actorOk1.id = Ids.generate();
    actorOk1.surname = search;
    actorOk1.name = RND.str(10);

    var actorOk2 = new VoiceActor();
    actorOk2.id = Ids.generate();
    actorOk2.surname = RND.str(10);
    actorOk2.name = search;

    var actorLeft = rndActor();

    ins(actorLeft, actorOk1, actorOk2);

    var index = ElasticSearch.actor();

    {
      GetResponse response = ElasticSearch.client()
        .get(new GetRequest().id(actorOk1.id).index(index), RequestOptions.DEFAULT);

      assertThat(response.isExists()).isTrue();
    }
    {
      GetResponse response = ElasticSearch.client()
        .get(new GetRequest().id(actorOk2.id).index(index), RequestOptions.DEFAULT);

      assertThat(response.isExists()).isTrue();
    }

    //
    //
    var actors = actorRegister.searchActor(search);
    //
    //

    var map = actors.stream().collect(Collectors.toMap(x -> x.id, x -> x));

    {
      var actor = map.get(actorOk1.id);
      assertThat(actor.name).isEqualTo(actorOk1.name);
      assertThat(actor.surname).isEqualTo(actorOk1.surname);
    }

    {
      var actor = map.get(actorOk2.id);
      assertThat(actor.name).isEqualTo(actorOk2.name);
      assertThat(actor.surname).isEqualTo(actorOk2.surname);
    }

  }

}
