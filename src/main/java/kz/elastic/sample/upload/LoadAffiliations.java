package kz.elastic.sample.upload;

import kz.elastic.sample.register.AffiliationRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoadAffiliations {

  // region Autowired fields
  @Autowired
  private AffiliationRegister affiliationRegister;
  // endregion

  public void loadAffiliations() throws Exception {
    System.out.println("Affiliations is OK!");
  }

}
