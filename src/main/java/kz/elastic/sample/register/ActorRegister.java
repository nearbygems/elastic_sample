package kz.elastic.sample.register;

import kz.elastic.sample.model.VoiceActor;

import java.io.IOException;
import java.util.List;

public interface ActorRegister {

  void addActor(VoiceActor actor) throws IOException;

  List<VoiceActor> searchActor(String search) throws IOException;

}
