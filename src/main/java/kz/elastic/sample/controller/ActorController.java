package kz.elastic.sample.controller;

import kz.elastic.sample.model.VoiceActor;
import kz.elastic.sample.register.ActorRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/actor")
public class ActorController {

  // region Autowired fields
  @Autowired
  private ActorRegister actorRegister;
  // endregion

  @GetMapping("/search")
  public List<VoiceActor> searchActor(@RequestParam("search") String search) throws IOException {
    return actorRegister.searchActor(search);
  }

}
