package kz.elastic.sample.impl;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.Affiliation;
import kz.elastic.sample.model.DevilFruit;
import kz.elastic.sample.model.Person;
import kz.elastic.sample.model.VoiceActor;
import kz.elastic.sample.register.PersonRegister;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Component;

@Component
public class PersonRegisterImpl implements PersonRegister {

  @Override
  public void addPerson(Person person) throws Exception {

    var json = XContentFactory.jsonBuilder();

    json.startObject();

    json.field(Person.ES_NAME, person.name);
    json.field(Person.ES_ALIAS, person.alias);
    json.field(Person.ES_EPITHET, person.epithet);

    json.field(Person.ES_AGE, person.age);
    json.field(Person.ES_HEIGHT, person.height);
    json.field(Person.ES_BIRTH_DAY, person.birthday);

    json.startObject(Person.ES_AFFILIATIONS);
    {
      for (var affiliation : person.affiliations) {

        json.startObject(affiliation.id);
        {
          json.field(Affiliation.ES_NAME, affiliation.name);
          json.field(Affiliation.ES_CAPTAIN, affiliation.captain);
          json.field(Affiliation.ES_SHIP, affiliation.shipName);
          json.field(Affiliation.ES_BOUNTY, affiliation.bounty);
        }
        json.endObject();

      }
    }
    json.endObject();

    json.startObject(Person.ES_ACTORS);
    {
      for (var actor : person.actors) {
        json.startObject(actor.id);
        {
          json.field(VoiceActor.ES_SURNAME, actor.surname);
          json.field(VoiceActor.ES_NAME, actor.name);
        }
        json.endObject();
      }
    }
    json.endObject();

    json.field(Person.ES_BLOOD_TYPE, person.bloodType);
    json.field(Person.ES_BOUNTY, person.bounty);
    json.field(Person.ES_STATUS, person.status);

    json.startObject(Person.ES_FRUITS);
    {
      for (var fruit : person.fruits) {
        json.startObject(fruit.id);
        {
          json.field(DevilFruit.ES_NAME, fruit.name);
          json.field(DevilFruit.ES_MEANING, fruit.meaning);
          json.field(DevilFruit.ES_TYPE, fruit.type);
        }
        json.endObject();
      }
    }
    json.endObject();

    json.endObject();

    var indexRequest = new IndexRequest(ElasticSearch.person());

    indexRequest.id(person.id);

    indexRequest.source(json);

    ElasticSearch.client().index(indexRequest, RequestOptions.DEFAULT);

  }

}

