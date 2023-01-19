package kz.nearbygems.elastic.models.web;

import lombok.ToString;


@ToString
public class Page {

  public int offset;
  public int limit;

  public static Page of(int from, int size) {

    final var page = new Page();
    page.offset = from;
    page.limit  = size;
    return page;
  }

}
