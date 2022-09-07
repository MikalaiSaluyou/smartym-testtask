package com.smartym.testtask.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;

public class CustomRequestEntityConverter
    implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {
  private static final String SCOPE_VALUE = "pisp";
  private final OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

  public CustomRequestEntityConverter() {
    defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
  }

  @Override
  public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
    final RequestEntity<?> entity = defaultConverter.convert(req);

    final MultiValueMap<String, String> params = (MultiValueMap<String, String>) entity.getBody();

    params.add(OAuth2ParameterNames.SCOPE, SCOPE_VALUE);
    return new RequestEntity<>(params, entity.getHeaders(), entity.getMethod(), entity.getUrl());
  }
}
