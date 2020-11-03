package kz.elastic.sample.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DevilFruit {

  public String id;

  public String name;
  public String meaning;
  public FruitType type;

  public static String ES_NAME = "name";
  public static String ES_MEANING = "meaning";
  public static String ES_TYPE = "type";

}
