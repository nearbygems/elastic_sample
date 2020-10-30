package kz.elastic.sample.util;

import kz.greetgo.util.RND;

public class Ids {
  public static String generate() {
    return RND.str(10);
  }
}
