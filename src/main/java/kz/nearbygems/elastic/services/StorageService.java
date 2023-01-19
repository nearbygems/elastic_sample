package kz.nearbygems.elastic.services;

import kz.nearbygems.elastic.models.web.StorageRecord;

import java.util.List;


public interface StorageService {

  List<StorageRecord> load();

  StorageRecord loadById(String id);

  String save(StorageRecord record);

  void delete(String id);

}
