package kz.elastic.sample.register;

import kz.elastic.sample.model.Affiliation;

import java.io.IOException;

public interface AffiliationRegister {

  void addAffiliation(Affiliation affiliation) throws IOException;

}
