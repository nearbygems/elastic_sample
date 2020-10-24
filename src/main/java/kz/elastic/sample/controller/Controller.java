package kz.elastic.sample.controller;

import kz.elastic.sample.register.ElasticRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/elastic")
public class Controller {

  @Autowired
  private ElasticRegister elasticRegister;
}
