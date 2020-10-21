package kz.elastic.sample.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Character {

  public String name;
  public String alias;
  public String epithet;

  public int age;
  public int height;
  public Date birthday;

  public List<Affiliation> affiliations;
  public VoiceActor actor;

  public BloodType bloodType;
  public long bounty;
  public Status status;

}
