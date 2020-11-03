package kz.elastic.sample.register;

import kz.elastic.sample.model.Person;

import java.io.IOException;

public interface PersonRegister {

  void addPerson(Person personage) throws IOException;

}
