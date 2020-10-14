package kz.elastic.sample.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticSearch {

  private final RestHighLevelClient client;

  private static ElasticSearch instance;

  private ElasticSearch(RestHighLevelClient client) {
    this.client = client;
  }

  public static ElasticSearch getInstance() {

    if (instance == null) {

      RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200)));

      instance = new ElasticSearch(client);
    }

    return instance;
  }

  public static RestHighLevelClient client() {
    return getInstance().client;
  }

  public static String index() {
    return "human";
  }

}
