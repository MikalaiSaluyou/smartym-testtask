package com.smartym.testtask.service;

import com.smartym.testtask.model.PaymentRequest;
import java.io.IOException;

public interface PaymentService {
  void doPayment(final PaymentRequest request, final String accessToken) throws IOException;
}
