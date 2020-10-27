package kz.elastic.sample.impl;

import kz.elastic.sample.elastic.ElasticSearch;
import kz.elastic.sample.model.Affiliation;
import kz.elastic.sample.model.DevilFruit;
import kz.elastic.sample.model.Personage;
import kz.elastic.sample.model.VoiceActor;
import kz.elastic.sample.register.ElasticRegister;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Component;

@Component
public class ElasticRegisterImpl implements ElasticRegister {

  @Override
  public void addPersonage(Personage personage) throws Exception {

    var json = XContentFactory.jsonBuilder();

    json.startObject();

    json.field(Personage.ES_NAME, personage.name);
    json.field(Personage.ES_ALIAS, personage.alias);
    json.field(Personage.ES_EPITHET, personage.epithet);

    json.field(Personage.ES_AGE, personage.age);
    json.field(Personage.ES_HEIGHT, personage.height);
    json.field(Personage.ES_BIRTH_DAY, personage.birthday);

    json.startObject(Personage.ES_AFFILIATIONS);
    {
      for (var affiliation : personage.affiliations) {

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

    if (personage.actor != null) {
      json.startObject(Personage.ES_ACTOR);
      {
        json.field(VoiceActor.ES_SURNAME, personage.actor.surname);
        json.field(VoiceActor.ES_NAME, personage.actor.name);
      }
      json.endObject();
    }


    json.field(Personage.ES_BLOOD_TYPE, personage.bloodType);
    json.field(Personage.ES_BOUNTY, personage.bounty);
    json.field(Personage.ES_STATUS, personage.status);

    if (personage.fruit != null) {
      json.startObject(Personage.ES_FRUIT);
      {
        json.field(DevilFruit.ES_NAME, personage.fruit.name);
        json.field(DevilFruit.ES_MEANING, personage.fruit.meaning);
        json.field(DevilFruit.ES_TYPE, personage.fruit.type);
      }
      json.endObject();
    }

    json.endObject();

    var indexRequest = new IndexRequest(ElasticSearch.personage());

    indexRequest.id(personage.id);

    indexRequest.source(json);

    ElasticSearch.client().index(indexRequest, RequestOptions.DEFAULT);

  }

}

