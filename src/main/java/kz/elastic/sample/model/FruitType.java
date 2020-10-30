package kz.elastic.sample.model;

import java.util.List;

public enum FruitType {
  PARAMECIA,
  LOGIA,
  ANCIENT_ZOAN,
  MYTHICAL_ZOAN,
  ARTIFICIAL_ZOAN;

  public static List<FruitType> types() {
    return List.of(FruitType.PARAMECIA, FruitType.LOGIA, FruitType.ANCIENT_ZOAN,
      FruitType.ARTIFICIAL_ZOAN, FruitType.MYTHICAL_ZOAN);
  }

}
