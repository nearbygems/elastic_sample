package kz.elastic.sample.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class VoiceActor {

  public String id;

  public String surname;
  public String name;

  public static String ES_SURNAME = "surname";
  public static String ES_NAME = "name";

}
