package kz.nearbygems.elastic.services;

import kz.nearbygems.elastic.models.web.SourceRecord;


public interface SourceService {

  SourceRecord loadById(String id);

  String save(SourceRecord record);

}
