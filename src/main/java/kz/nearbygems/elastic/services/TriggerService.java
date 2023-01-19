package kz.nearbygems.elastic.services;

import kz.nearbygems.elastic.models.web.TriggerRecord;


public interface TriggerService {

  TriggerRecord loadById(String id);

  String save(TriggerRecord record);

}
