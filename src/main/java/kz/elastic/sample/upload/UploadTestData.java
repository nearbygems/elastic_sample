package kz.elastic.sample.upload;

import kz.elastic.sample.SampleApplication;
import kz.elastic.sample.elastic.ElasticSearch;
import org.springframework.boot.SpringApplication;

public class UploadTestData {
  public static void main(String[] args) {
    try (var context = SpringApplication.run(SampleApplication.class, args)) {
      context.getBean(TestDataLoader.class).loadTestData();
      ElasticSearch.client().close();
    } catch (Exception e) {
      throw new RuntimeException("");
    }
  }
}
