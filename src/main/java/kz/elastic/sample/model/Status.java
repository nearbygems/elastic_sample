package kz.elastic.sample.model;

import java.util.List;

public enum Status {
  ALIVE,
  DEAD;

  public static List<Status> types() {
    return List.of(Status.ALIVE, Status.DEAD);
  }

}
