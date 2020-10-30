package kz.elastic.sample.model;

import java.util.List;

public enum BloodType {
  X, F, S, XF;

  public static List<BloodType> types() {
    return List.of(BloodType.X, BloodType.F, BloodType.S, BloodType.XF);
  }

}
