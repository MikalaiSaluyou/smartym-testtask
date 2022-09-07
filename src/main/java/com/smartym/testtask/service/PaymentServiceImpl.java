package com.smartym.testtask.service;

import com.smartym.testtask.client.PaymentClient;
import com.smartym.testtask.exception.PaymentException;
import com.smartym.testtask.model.PaymentRequest;
import com.smartym.testtask.model.PaymentResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
  private static final String BEARER = "Bearer ";
  private final PaymentClient paymentClient;

  @Autowired
  public PaymentServiceImpl(PaymentClient paymentClient) {
    this.paymentClient = paymentClient;
  }

  public void doPayment(final PaymentRequest request, final String accessToken) throws IOException {

    final PaymentResponse paymentResponse =
        paymentClient.postPayment(request, BEARER.concat(accessToken));

    if (!paymentResponse.getStatus().equals("OK")) {
      throw new PaymentException("Status of payment is not OK");
    }
  }
}
