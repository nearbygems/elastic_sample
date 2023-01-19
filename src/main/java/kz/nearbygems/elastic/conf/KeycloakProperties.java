package kz.nearbygems.elastic.conf;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;


@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

  private final String url;
  private final String id;
  private final String secret;
  private final String realm;
  private final String username;
  private final String password;

}
