package kz.elastic.sample.controller;

import kz.elastic.sample.register.AffiliationRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/affiliation")
public class AffiliationController {

  // region Autowired fields
  @Autowired
  private AffiliationRegister affiliationRegister;
  // endregion

}
