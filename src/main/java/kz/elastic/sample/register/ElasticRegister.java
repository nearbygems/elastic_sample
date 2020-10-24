package kz.elastic.sample.register;

import kz.elastic.sample.model.Personage;

public interface ElasticRegister {

  void addPersonage(Personage personage) throws Exception;
}
