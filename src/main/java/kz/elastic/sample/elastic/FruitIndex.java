package kz.elastic.sample.elastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FruitIndex {

  // region Autowired fields
  @Autowired
  protected ElasticSearch elasticSearch;
  // endregion

  public Ensure ensure(Object companyId) {
    return structureMap.computeIfAbsent(Ids.idToObjectVariant(companyId), this::createIndex);
  }

}
