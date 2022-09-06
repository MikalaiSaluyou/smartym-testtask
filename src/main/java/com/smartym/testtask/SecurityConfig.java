package com.smartym.testtask;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private static final String[] REQUIRES_AUTHENTICATION = {"/payment/pay"};
  private static final String[] WHITE_LIST = {
    "/payment/index", "/payment/success", "/payment/form"
  };

  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.securityContext(
        context ->
            context.securityContextRepository(new RequestAttributeSecurityContextRepository()));
    http.headers().httpStrictTransportSecurity().maxAgeInSeconds(0).includeSubDomains(true);
    http.authorizeHttpRequests()
        .antMatchers(WHITE_LIST)
        .permitAll()
        .antMatchers(REQUIRES_AUTHENTICATION)
        .authenticated();
    http.oauth2Login(Customizer.withDefaults());
    http.oauth2Client(
        configurer ->
            configurer
                .authorizationCodeGrant()
                .accessTokenResponseClient(this.authorizationTokenResponseClient()));

    return http.build();
  }

  public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>
      authorizationTokenResponseClient() {
    final OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
        new OAuth2AccessTokenResponseHttpMessageConverter();

    tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(
        new CustomAccessTokenResponseConverter());

    final RestTemplate restTemplate =
        new RestTemplate(
            Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

    final DefaultAuthorizationCodeTokenResponseClient tokenResponseClient =
        new DefaultAuthorizationCodeTokenResponseClient();
    tokenResponseClient.setRestOperations(restTemplate);
    tokenResponseClient.setRequestEntityConverter(new CustomRequestEntityConverter());

    return tokenResponseClient;
  }
}
