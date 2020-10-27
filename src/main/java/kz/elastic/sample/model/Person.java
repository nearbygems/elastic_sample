package kz.elastic.sample.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Person {

  public String id;

  public String name;
  public String alias;
  public String epithet;

  public int age;
  public int height;
  public LocalDate birthday;

  public List<Affiliation> affiliations = new ArrayList<>();
  public VoiceActor actor;

  public BloodType bloodType;
  public long bounty;
  public Status status;

  public DevilFruit fruit;

  public static String ES_NAME = "name";
  public static String ES_ALIAS = "alias";
  public static String ES_EPITHET = "epithet";

  public static String ES_AGE = "age";
  public static String ES_HEIGHT = "height";
  public static String ES_BIRTH_DAY = "birthDay";

  public static String ES_AFFILIATIONS = "affiliations";
  public static String ES_ACTOR = "actor";

  public static String ES_BLOOD_TYPE = "bloodType";
  public static String ES_BOUNTY = "bounty";
  public static String ES_STATUS = "status";

  public static String ES_FRUIT = "fruit";

}
