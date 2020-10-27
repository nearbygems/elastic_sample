package kz.elastic.sample.upload;

import kz.elastic.sample.model.*;
import kz.elastic.sample.register.PersonRegister;
import kz.elastic.sample.util.Ids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LoadPersons {

  // region Autowired fields
  @Autowired
  private PersonRegister personRegister;
  // endregion

  public void loadPersons() throws Exception {
    var person = new Person();

    person.id = Ids.generate();

    person.name = "Roronoa Zoro";
    person.alias = "Zoro-juurou";
    person.epithet = "Pirate Hunter Zoro";

    person.age = 21;
    person.height = 181;
    person.birthday = LocalDate.of(1994, 11, 11);

    var affiliation = new Affiliation();
    affiliation.id = "eug08jvluo";
    affiliation.name = "Straw Hat Pirates";
    affiliation.captain = "Monkey D. Luffy";
    affiliation.shipName = "Thousand Sunny";
    affiliation.bounty = 3_161_000_100L;

    person.affiliations.add(affiliation);

    person.actor = new VoiceActor();
    person.actor.surname = "Nakai";
    person.actor.name = "Kazuya";

    person.bloodType = BloodType.XF;
    person.bounty = 320_000_000L;
    person.status = Status.ALIVE;

    //
    //
    personRegister.addPerson(person);
  }
}
