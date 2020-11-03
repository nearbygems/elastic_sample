package kz.elastic.sample.register;

import kz.elastic.sample.model.DevilFruit;

import java.io.IOException;

public interface FruitRegister {

  void addFruit(DevilFruit fruit) throws IOException;

}
