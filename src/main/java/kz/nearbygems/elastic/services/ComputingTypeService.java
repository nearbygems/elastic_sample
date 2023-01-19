package kz.nearbygems.elastic.services;

import kz.nearbygems.elastic.models.enums.SystemType;
import kz.nearbygems.elastic.models.web.ComputingTypeRecord;

import java.util.List;


public interface ComputingTypeService {

  List<ComputingTypeRecord> load(SystemType system);

  ComputingTypeRecord loadById(String id);

  String save(ComputingTypeRecord record);

  void delete(String id);

}
