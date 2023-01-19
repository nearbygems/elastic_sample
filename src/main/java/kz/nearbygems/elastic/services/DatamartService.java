package kz.nearbygems.elastic.services;

import kz.nearbygems.elastic.models.web.DatamartRecord;


public interface DatamartService {

  DatamartRecord loadById(String id);

  String save(DatamartRecord record);

}
