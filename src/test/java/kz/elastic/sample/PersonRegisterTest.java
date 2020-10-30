package kz.elastic.sample;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.Affiliation;
import kz.elastic.sample.model.DevilFruit;
import kz.elastic.sample.model.Person;
import kz.elastic.sample.model.VoiceActor;
import kz.elastic.sample.register.PersonRegister;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonRegisterTest extends SampleApplicationTests {

  // region Autowired fields
  @Autowired
  private PersonRegister personRegister;
  // endregion

  @Test
  void addPerson() throws Exception {

    var person = rndPerson();

    var affiliation1 = rndAffiliation(person);
    var affiliation2 = rndAffiliation(person);

    var fruit1 = rndFruit(person);
    var fruit2 = rndFruit(person);

    var actor1 = rndActor(person);
    var actor2 = rndActor(person);

    //
    //
    personRegister.addPerson(person);
    //
    //

    var getIndexRequest = new GetIndexRequest(ElasticSearch.person());
    var getIndexResponse = ElasticSearch.client().indices().get(getIndexRequest, RequestOptions.DEFAULT);

    System.out.println("\n50iz72dm4p :: index settings:");
    printSettings(getIndexResponse.getSettings().get(ElasticSearch.person()));

    var indexMatchMappings = getIndexResponse.getMappings().get(ElasticSearch.person());
    System.out.println("\nqamlpv5a6t :: index mappings:");
    printMappings(indexMatchMappings);

    var getRequest = new GetRequest(ElasticSearch.person());
    getRequest.id(person.id);
    var getResponse = ElasticSearch.client().get(getRequest, RequestOptions.DEFAULT);

    System.out.println("97hgum79kh :: getResponse.source() = " + prettyJson(getResponse.getSourceAsString()));
    var source = getResponse.getSource();

    assertThat(source.get(Person.ES_NAME)).isEqualTo(person.name);
    assertThat(source.get(Person.ES_ALIAS)).isEqualTo(person.alias);
    assertThat(source.get(Person.ES_EPITHET)).isEqualTo(person.epithet);

    assertThat(source.get(Person.ES_AGE)).isEqualTo(person.age);
    assertThat(source.get(Person.ES_HEIGHT)).isEqualTo(person.height);
    assertThat(source.get(Person.ES_BIRTH_DAY)).isEqualTo(person.birthday.toString());

    assertThat(source.get(Person.ES_BLOOD_TYPE)).isEqualTo(person.bloodType.toString());
    assertThat(source.get(Person.ES_BOUNTY)).isEqualTo(person.bounty);
    assertThat(source.get(Person.ES_STATUS)).isEqualTo(person.status.toString());

    var affiliations = castToMap(source.get(Person.ES_AFFILIATIONS));
    {
      var affiliation = castToMap(affiliations.get(affiliation1.id));
      assertThat(affiliation.get(Affiliation.ES_NAME)).isEqualTo(affiliation1.name);
      assertThat(affiliation.get(Affiliation.ES_CAPTAIN)).isEqualTo(affiliation1.captain);
      assertThat(affiliation.get(Affiliation.ES_SHIP)).isEqualTo(affiliation1.shipName);
      assertThat(affiliation.get(Affiliation.ES_BOUNTY)).isEqualTo(affiliation1.bounty);
    }
    {
      var affiliation = castToMap(affiliations.get(affiliation2.id));
      assertThat(affiliation.get(Affiliation.ES_NAME)).isEqualTo(affiliation2.name);
      assertThat(affiliation.get(Affiliation.ES_CAPTAIN)).isEqualTo(affiliation2.captain);
      assertThat(affiliation.get(Affiliation.ES_SHIP)).isEqualTo(affiliation2.shipName);
      assertThat(affiliation.get(Affiliation.ES_BOUNTY)).isEqualTo(affiliation2.bounty);
    }

    var fruits = castToMap(source.get(Person.ES_FRUITS));
    {
      var fruit = castToMap(fruits.get(fruit1.id));
      assertThat(fruit.get(DevilFruit.ES_NAME)).isEqualTo(fruit1.name);
      assertThat(fruit.get(DevilFruit.ES_MEANING)).isEqualTo(fruit1.meaning);
      assertThat(fruit.get(DevilFruit.ES_TYPE)).isEqualTo(fruit1.type.toString());
    }
    {
      var fruit = castToMap(fruits.get(fruit2.id));
      assertThat(fruit.get(DevilFruit.ES_NAME)).isEqualTo(fruit2.name);
      assertThat(fruit.get(DevilFruit.ES_MEANING)).isEqualTo(fruit2.meaning);
      assertThat(fruit.get(DevilFruit.ES_TYPE)).isEqualTo(fruit2.type.toString());
    }

    var actors = castToMap(source.get(Person.ES_ACTORS));
    {
      var actor = castToMap(actors.get(actor1.id));
      assertThat(actor.get(VoiceActor.ES_SURNAME)).isEqualTo(actor1.surname);
      assertThat(actor.get(VoiceActor.ES_NAME)).isEqualTo(actor1.name);
    }
    {
      var actor = castToMap(actors.get(actor2.id));
      assertThat(actor.get(VoiceActor.ES_SURNAME)).isEqualTo(actor2.surname);
      assertThat(actor.get(VoiceActor.ES_NAME)).isEqualTo(actor2.name);
    }

    deleteIndex(ElasticSearch.person());

  }

}
