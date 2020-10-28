package kz.elastic.sample.register;

import kz.elastic.sample.model.VoiceActor;

public interface ActorRegister {

  void addActor(VoiceActor actor) throws Exception;
}
