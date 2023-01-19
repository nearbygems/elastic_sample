package kz.nearbygems.elastic;

import kz.nearbygems.elastic.conf.KeycloakProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;


@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(KeycloakProperties.class)
public class CatalogApplication {

  public static void main(String[] args) {

    copyHunspell();
    SpringApplication.run(CatalogApplication.class, args);
  }

  private static void copyHunspell() {

    try {
      final var hunspell = new ClassPathResource("hunspell").getFile().getAbsolutePath();
      Runtime.getRuntime().exec("docker cp " + hunspell + " elasticsearch:/usr/share/elasticsearch/config/");
    } catch (IOException e) {
      log.error("Error occurred while copying hunspell files", e);
    }
  }

}
