package kz.elastic.sample.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Human {
  public String id;
  public String surname;
  public String name;
  public int age;
  public LocalDate birthDay;

  public static String ES_SURNAME = "surname";
  public static String ES_NAME = "name";
  public static String ES_AGE = "age";
  public static String ES_BIRTH_DAY = "birthDay";
}
