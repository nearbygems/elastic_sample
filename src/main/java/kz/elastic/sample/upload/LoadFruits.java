package kz.elastic.sample.upload;

import kz.elastic.sample.register.FruitRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoadFruits {

  // region Autowired fields
  @Autowired
  private FruitRegister fruitRegister;
  // endregion

  public void loadFruits() throws Exception {
    System.out.println("Fruits is OK!");
  }

}
