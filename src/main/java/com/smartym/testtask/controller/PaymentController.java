package com.smartym.testtask.controller;

import com.smartym.testtask.dto.PaymentDTO;
import com.smartym.testtask.exception.PaymentException;
import com.smartym.testtask.model.PaymentRequest;
import com.smartym.testtask.service.PaymentService;
import java.io.IOException;
import java.time.Instant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment")
public class PaymentController {
  private final PaymentService paymentService;
  private final ModelMapper modelMapper;

  private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

  @Autowired
  public PaymentController(
      final PaymentService paymentService,
      ModelMapper modelMapper,
      OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
    this.paymentService = paymentService;
    this.modelMapper = modelMapper;
    this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
  }

  @GetMapping("/index")
  public String index() {
    return "index";
  }

  @GetMapping("/auth")
  public String getPayment(@RequestParam("code") final String code, final Model model) {
    model.addAttribute("code", code);
    return "auth";
  }

  @GetMapping("/form")
  public String getPaymentForm(@ModelAttribute("paymentDTO") final PaymentDTO paymentDTO) {

    return "payment";
  }

  @PostMapping("/pay")
  public String postPayment(@ModelAttribute("paymentDTO") final PaymentDTO paymentDTO)
      throws IOException {

    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    final OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

    final OAuth2AuthorizedClient client =
        oAuth2AuthorizedClientService.loadAuthorizedClient(
            oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());
    final String accessToken = client.getAccessToken().getTokenValue();

    /*
     * Here I build a dummy request that differs from the Swagger payload model. 'pis-controller' accepts any kind of request
     * body, so just for sake of time I decided to simplify the solution that way.
     */
    final PaymentRequest paymentRequest = buildDummyPaymentRequest(paymentDTO);
    paymentService.postPayment(paymentRequest, accessToken);

    return "success";
  }

  @GetMapping("/success")
  public String getSuccess() {
    return "success";
  }

  @ExceptionHandler(IOException.class)
  public String handleError(final Model model) {
    model.addAttribute("errorMessage", "Ups, something unexpected happened!");
    return "failure";
  }

  @ExceptionHandler(PaymentException.class)
  public String handleRuntimeError(final Model model, final RuntimeException e) {
    model.addAttribute("errorMessage", e.getMessage());
    return "failure";
  }

  private PaymentRequest buildDummyPaymentRequest(final PaymentDTO paymentDTO) {
    final PaymentRequest paymentRequest = modelMapper.map(paymentDTO, PaymentRequest.class);
    paymentRequest.setCreationDateTime(Instant.now());

    return paymentRequest;
  }
}
