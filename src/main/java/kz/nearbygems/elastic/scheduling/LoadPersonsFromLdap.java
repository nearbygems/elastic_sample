package kz.nearbygems.elastic.scheduling;

import kz.nearbygems.elastic.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
@RequiredArgsConstructor
public class LoadPersonsFromLdap {

  private final PersonService service;

  @Scheduled(cron = "0 0 1 * * SUN")
  public void execute() {

    service.loadPersonsFromLdap();
  }

}
