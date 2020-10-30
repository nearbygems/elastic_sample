package kz.elastic.sample.model;

public class Affiliation {

  public String id;

  public String name;
  public String captain;
  public String shipName;
  public long bounty;

  public static String ES_NAME = "name";
  public static String ES_CAPTAIN = "captain";
  public static String ES_SHIP = "ship";
  public static String ES_BOUNTY = "bounty";

}
