package kz.elastic.sample.upload;

import kz.elastic.sample.register.ActorRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoadActors {

  // region Autowired fields
  @Autowired
  private ActorRegister actorRegister;
  // endregion

  public void loadActors() throws Exception {
    System.out.println("Actors is OK!");
  }

}
