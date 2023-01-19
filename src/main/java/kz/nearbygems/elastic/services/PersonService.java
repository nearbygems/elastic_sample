package kz.nearbygems.elastic.services;

import kz.nearbygems.elastic.models.web.Persons;


public interface PersonService {

  void loadPersonsFromLdap();

  Persons search(String text, int offset, int limit);

  Persons.Record loadById(String id);

  String save(Persons.Record record);

  void delete(String id);

}
