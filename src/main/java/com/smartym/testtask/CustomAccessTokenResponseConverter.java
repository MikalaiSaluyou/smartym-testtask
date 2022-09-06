package com.smartym.testtask;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;

public class CustomAccessTokenResponseConverter
    implements Converter<Map<String, Object>, OAuth2AccessTokenResponse> {
  private static final String ACCESS_TOKEN = "accessToken";
  private static final String TOKEN_TYPE = "tokenType";
  private static final String EXPIRES_IN = "expiresIn";
  private static final String REFRESH_TOKEN = "refreshToken";

  private static final Set<String> TOKEN_RESPONSE_PARAMETER_NAMES =
      Stream.of(ACCESS_TOKEN, TOKEN_TYPE, EXPIRES_IN, REFRESH_TOKEN, OAuth2ParameterNames.SCOPE)
          .collect(Collectors.toSet());

  @Override
  public OAuth2AccessTokenResponse convert(final Map<String, Object> tokenResponseParameters) {
    final String accessToken = (String) tokenResponseParameters.get(ACCESS_TOKEN);
    final OAuth2AccessToken.TokenType accessTokenType = OAuth2AccessToken.TokenType.BEARER;

    long expiresIn = 0;
    if (tokenResponseParameters.containsKey(EXPIRES_IN)) {
      try {
        expiresIn = Long.parseLong((String) tokenResponseParameters.get(EXPIRES_IN));
      } catch (NumberFormatException ignored) {
      }
    }

    Set<String> scopes = Collections.emptySet();
    if (tokenResponseParameters.containsKey(OAuth2ParameterNames.SCOPE)) {
      String scope = (String) tokenResponseParameters.get(OAuth2ParameterNames.SCOPE);
      scopes =
          Arrays.stream(StringUtils.delimitedListToStringArray(scope, " "))
              .collect(Collectors.toSet());
    }

    final Map<String, Object> additionalParameters = new LinkedHashMap<>();
    tokenResponseParameters.entrySet().stream()
        .filter(e -> !TOKEN_RESPONSE_PARAMETER_NAMES.contains(e.getKey()))
        .forEach(e -> additionalParameters.put(e.getKey(), e.getValue()));

    return OAuth2AccessTokenResponse.withToken(accessToken)
        .tokenType(accessTokenType)
        .expiresIn(expiresIn)
        .scopes(scopes)
        .additionalParameters(additionalParameters)
        .build();
  }
}
