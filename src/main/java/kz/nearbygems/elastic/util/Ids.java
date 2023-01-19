package kz.nearbygems.elastic.util;

import java.util.UUID;


public class Ids {

  public static String generate() {

    return UUID.randomUUID().toString();
  }

}
