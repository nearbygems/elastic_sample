package kz.elastic.sample.util;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Ids {
  public static String generate() {
    var array = new byte[10];
    new Random().nextBytes(new byte[10]);
    return new String(array, StandardCharsets.UTF_8);
  }
}
