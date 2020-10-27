package kz.elastic.sample.upload;

import kz.elastic.sample.model.*;
import kz.elastic.sample.register.ElasticRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TestDataLoader {

  // region Autowired fields
  @Autowired
  private ElasticRegister elasticRegister;
  // endregion

  public void loadTestData() throws Exception {
    var personage = new Personage();

    personage.id = "h8enwt9fqp";

    personage.name = "Roronoa Zoro";
    personage.alias = "Zoro-juurou";
    personage.epithet = "Pirate Hunter Zoro";

    personage.age = 21;
    personage.height = 181;
    personage.birthday = LocalDate.of(1994, 11, 11);

    var affiliation = new Affiliation();
    affiliation.id = "eug08jvluo";
    affiliation.name = "Straw Hat Pirates";
    affiliation.captain = "Monkey D. Luffy";
    affiliation.shipName = "Thousand Sunny";
    affiliation.bounty = 3_161_000_100L;

    personage.affiliations.add(affiliation);

    personage.actor = new VoiceActor();
    personage.actor.surname = "Nakai";
    personage.actor.name = "Kazuya";

    personage.bloodType = BloodType.XF;
    personage.bounty = 320_000_000L;
    personage.status = Status.ALIVE;

    //
    //
    elasticRegister.addPersonage(personage);
  }
}
