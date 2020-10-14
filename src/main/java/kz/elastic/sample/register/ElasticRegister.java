package kz.elastic.sample.register;

import kz.elastic.sample.model.Human;

public interface ElasticRegister {

  void addHuman(Human human) throws Exception;
}
