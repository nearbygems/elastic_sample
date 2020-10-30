package kz.elastic.sample.controller;

import kz.elastic.sample.register.FruitRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fruit")
public class FruitController {

  // region Autowired fields
  @Autowired
  private FruitRegister fruitRegister;
  // endregion

}
