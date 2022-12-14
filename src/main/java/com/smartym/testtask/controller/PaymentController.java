package com.smartym.testtask.controller;

import com.smartym.testtask.dto.PaymentDTO;
import com.smartym.testtask.exception.PaymentException;
import com.smartym.testtask.model.PaymentRequest;
import com.smartym.testtask.service.PaymentService;
import java.io.IOException;
import java.time.Instant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payment")
public class PaymentController {
  private final PaymentService paymentService;
  private final ModelMapper modelMapper;

  @Autowired
  public PaymentController(final PaymentService paymentService, ModelMapper modelMapper) {
    this.paymentService = paymentService;
    this.modelMapper = modelMapper;
  }

  @GetMapping("/index")
  public String index() {
    return "index";
  }

  @GetMapping("/form")
  public String getPaymentForm(
      @ModelAttribute("paymentDTO") final PaymentDTO paymentDTO,
      @RegisteredOAuth2AuthorizedClient("smartym") final OAuth2AuthorizedClient authorizedClient) {

    return "payment";
  }

  @PostMapping("/pay")
  public String postPayment(
      @ModelAttribute("paymentDTO") final PaymentDTO paymentDTO,
      @RegisteredOAuth2AuthorizedClient("smartym")
          final OAuth2AuthorizedClient oAuth2AuthorizedClient)
      throws IOException {
    final String accessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
    /*
     * Here I build a dummy request that differs from the Swagger payload model. 'pis-controller' accepts any kind of request
     * body, so just for sake of time I decided to simplify the solution that way.
     */
    final PaymentRequest paymentRequest = buildDummyPaymentRequest(paymentDTO);
    paymentService.doPayment(paymentRequest, accessToken);

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
