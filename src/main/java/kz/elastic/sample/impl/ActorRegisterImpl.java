package kz.elastic.sample.impl;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.VoiceActor;
import kz.elastic.sample.register.ActorRegister;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Component;

@Component
public class ActorRegisterImpl implements ActorRegister {

  @Override
  public void addActor(VoiceActor actor) throws Exception {

    var json = XContentFactory.jsonBuilder();

    json.startObject();

    json.field(VoiceActor.ES_SURNAME, actor.surname);
    json.field(VoiceActor.ES_NAME, actor.name);

    json.endObject();

    var indexRequest = new IndexRequest(ElasticSearch.person());

    indexRequest.id(actor.id);

    indexRequest.source(json);

    ElasticSearch.client().index(indexRequest, RequestOptions.DEFAULT);

  }

}
