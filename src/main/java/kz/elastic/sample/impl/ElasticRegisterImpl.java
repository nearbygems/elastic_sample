package kz.elastic.sample.impl;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.Human;
import kz.elastic.sample.register.ElasticRegister;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Component;

@Component
public class ElasticRegisterImpl implements ElasticRegister {

  @Override
  public void addHuman(Human human) throws Exception {

    var builder = XContentFactory.jsonBuilder()
      .startObject()
      .field(Human.ES_SURNAME, human.surname)
      .field(Human.ES_NAME, human.name)
      .field(Human.ES_AGE, human.age)
      .field(Human.ES_BIRTH_DAY, human.birthDay)
      .endObject();

    var indexRequest = new IndexRequest(ElasticSearch.index());

    indexRequest.id(human.id);

    indexRequest.source(builder);

    ElasticSearch.client().index(indexRequest, RequestOptions.DEFAULT);
  }

}

