package kz.elastic.sample.impl;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.DevilFruit;
import kz.elastic.sample.register.FruitRegister;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FruitRegisterImpl implements FruitRegister {

  @Override
  public void addFruit(DevilFruit fruit) throws IOException {

    var json = XContentFactory.jsonBuilder();

    json.startObject();

    json.field(DevilFruit.ES_NAME, fruit.name);
    json.field(DevilFruit.ES_MEANING, fruit.meaning);
    json.field(DevilFruit.ES_TYPE, fruit.type);

    json.endObject();

    var indexRequest = new IndexRequest(ElasticSearch.fruit())
      .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

    indexRequest.id(fruit.id);

    indexRequest.source(json);

    ElasticSearch.client().index(indexRequest, RequestOptions.DEFAULT);

  }

}
