package kz.elastic.sample.elastic;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;

public class ElasticSearch {

  private final RestHighLevelClient client;

  private static ElasticSearch instance;

  private ElasticSearch(RestHighLevelClient client) {
    this.client = client;
  }

  public static ElasticSearch getInstance() {

    if (instance == null) {

      var defaultHeaders = new Header[]{new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
        new BasicHeader(HttpHeaders.ACCEPT_CHARSET, "UTF-8")};

      var builder = RestClient.builder(new HttpHost("localhost", 9200, "http"));
      builder.setDefaultHeaders(defaultHeaders);

      var client = new RestHighLevelClient(builder);

      instance = new ElasticSearch(client);

    }

    return instance;

  }

  public static RestHighLevelClient client() {
    return getInstance().client;
  }

  public static String person() {
    return "person";
  }

  public static String actor() { return "actor"; }

  public static String affiliation() { return "affiliation"; }

  public static String fruit() { return "fruit"; }

  public static List<String> indexes() { return List.of(person(), actor(), affiliation(), fruit()); }

}
