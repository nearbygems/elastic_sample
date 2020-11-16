package kz.elastic.sample.impl;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.VoiceActor;
import kz.elastic.sample.register.ActorRegister;
import lombok.SneakyThrows;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ActorRegisterImpl implements ActorRegister {

  @Override
  @SneakyThrows
  public void addActor(VoiceActor actor) {

    var json = XContentFactory.jsonBuilder();

    json.startObject();

    json.field(VoiceActor.ES_SURNAME, actor.surname);
    json.field(VoiceActor.ES_NAME, actor.name);

    json.endObject();

    var indexRequest = new IndexRequest(ElasticSearch.actor())
      .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

    indexRequest.id(actor.id);

    indexRequest.source(json);

    ElasticSearch.client().index(indexRequest, RequestOptions.DEFAULT);

  }

  @Override
  @SneakyThrows
  public List<VoiceActor> searchActor(String search) {

    var searchResponse = requestSearchResponse(search);

    var ret = new ArrayList<VoiceActor>();

    if (searchResponse == null || searchResponse.getHits().getTotalHits().value == 0) {
      return ret;
    }

    for (var hit : searchResponse.getHits().getHits()) {
      ret.add(getActor(hit));
    }

    return ret;

  }

  @SneakyThrows
  private SearchResponse requestSearchResponse(String search) {

    var query = makeSearchQuery(search);

    var sourceBuilder = new SearchSourceBuilder();
    sourceBuilder.query(query);

    var searchRequest = new SearchRequest();
    searchRequest.indices(ElasticSearch.actor());
    searchRequest.source(sourceBuilder);

    System.out.println("gurkzun7wb :: searchRequest = " + Strings.toString(searchRequest.source(), true, true));

    try {
      return ElasticSearch.client().search(searchRequest, RequestOptions.DEFAULT);
    } catch (ElasticsearchStatusException e) {
      return null;
    }

  }

  private QueryBuilder makeSearchQuery(String search) {

    var query = QueryBuilders.boolQuery();
    query.should(QueryBuilders.matchQuery(VoiceActor.ES_SURNAME, search));
    query.should(QueryBuilders.matchQuery(VoiceActor.ES_NAME, search));
    return query;

  }

  private VoiceActor getActor(SearchHit hit) {
    var source = hit.getSourceAsMap();
    var ret = new VoiceActor();
    ret.id = hit.getId();
    ret.surname = source.get(VoiceActor.ES_SURNAME).toString();
    ret.name = source.get(VoiceActor.ES_NAME).toString();
    return ret;
  }

}
