package kz.nearbygems.elastic.services;

import kz.nearbygems.elastic.models.web.DataCatalogRecord;
import kz.nearbygems.elastic.models.web.DataRecordList;
import kz.nearbygems.elastic.models.web.SearchRequest;

import java.util.List;


public interface CatalogService {

  DataRecordList search(SearchRequest request);

  long count(DataCatalogRecord.DocTypeRecord docType);

  void delete(String id);

  List<DataCatalogRecord.StagingRecord.ColumnRecord> loadFields(String id, String name);

}
