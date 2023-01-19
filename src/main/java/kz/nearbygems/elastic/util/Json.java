package kz.nearbygems.elastic.util;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Json {

  public static final ObjectMapper MAPPER = new ObjectMapper();

  public static String dot(String... fields) {

    return String.join(".", fields);
  }

}
