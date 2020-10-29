package kz.elastic.sample.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDataLoader {

  // region Autowired fields
  @Autowired
  private LoadAffiliations loadAffiliations;

  @Autowired
  private LoadPersons loadPersons;

  @Autowired
  private LoadFruits loadFruits;

  @Autowired
  private LoadActors loadActors;
  // endregion

  public void loadTestData() throws Exception {
    loadAffiliations.loadAffiliations();
    loadPersons.loadPersons();
    loadFruits.loadFruits();
    loadActors.loadActors();
  }

}
