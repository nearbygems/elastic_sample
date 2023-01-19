package kz.nearbygems.elastic.services;

import kz.nearbygems.elastic.models.web.TagRecord;

import java.util.List;


public interface TagService {

  List<TagRecord> load();

  TagRecord loadById(String id);

  String save(TagRecord record);

  void delete(String id);

}
