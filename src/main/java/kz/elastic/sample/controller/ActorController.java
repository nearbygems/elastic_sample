package kz.elastic.sample.controller;

import kz.elastic.sample.register.ActorRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/person")
public class ActorController {

  // region Autowired fields
  @Autowired
  private ActorRegister actorRegister;
  // endregion

}
