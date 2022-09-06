package com.smartym.testtask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import java.time.Instant;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
@EnableWebSecurity
public class Config {

  @Value("${base.url}")
  private String baseUrl;

  @Bean
  public Retrofit retrofit() {
    final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    httpClient.addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY));

    final Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(
                Instant.class,
                (JsonDeserializer<Instant>)
                    (json, typeOfT, context) ->
                        Instant.ofEpochSecond(json.getAsJsonPrimitive().getAsLong()))
            .registerTypeAdapter(
                Instant.class,
                (JsonSerializer<Instant>)
                    (date, type, jsonSerializationContext) -> new JsonPrimitive(date.toString()))
            .create();

    return new Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient.build())
        .build();
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  /* @Bean
  public OAuth2AuthorizedClientManager authorizedClientManager(
      final ClientRegistrationRepository clientRegistrationRepository,
      final OAuth2AuthorizedClientRepository authorizedClientRepository) {

    final OAuth2AuthorizedClientProvider authorizedClientProvider =
        OAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode()
            .build();

    final DefaultOAuth2AuthorizedClientManager authorizedClientManager =
        new DefaultOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientRepository);
    authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

    return authorizedClientManager;
  }*/
}
