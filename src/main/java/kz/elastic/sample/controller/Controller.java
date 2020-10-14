package kz.elastic.sample.controller;

import kz.elastic.sample.model.Human;
import kz.elastic.sample.register.ElasticRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/elastic")
public class Controller {

  @Autowired
  private ElasticRegister elasticRegister;

  @PostMapping("/humans")
  public void addHuman(@RequestParam("human") Human human) throws Exception { elasticRegister.addHuman(human); }

  @GetMapping("/humans")
  public List<Human> getHumans() {


    return null;

  }

  @PutMapping("/humans")
  public void updateHumans(@RequestParam("humans") List<Human> humans) {


  }

  @DeleteMapping("/humans")
  public void deleteHumans() {


  }
}
