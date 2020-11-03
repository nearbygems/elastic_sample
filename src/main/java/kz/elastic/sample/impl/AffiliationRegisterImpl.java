package kz.elastic.sample.impl;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.Affiliation;
import kz.elastic.sample.register.AffiliationRegister;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AffiliationRegisterImpl implements AffiliationRegister {

  @Override
  public void addAffiliation(Affiliation affiliation) throws IOException {

    var json = XContentFactory.jsonBuilder();

    json.startObject();

    json.field(Affiliation.ES_NAME, affiliation.name);
    json.field(Affiliation.ES_CAPTAIN, affiliation.captain);
    json.field(Affiliation.ES_SHIP, affiliation.shipName);
    json.field(Affiliation.ES_BOUNTY, affiliation.bounty);

    json.endObject();

    var indexRequest = new IndexRequest(ElasticSearch.affiliation());

    indexRequest.id(affiliation.id);

    indexRequest.source(json);

    ElasticSearch.client().index(indexRequest, RequestOptions.DEFAULT);

  }

}
