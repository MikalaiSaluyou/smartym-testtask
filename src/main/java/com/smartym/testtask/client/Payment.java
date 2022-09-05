package com.smartym.testtask.client;

import com.smartym.testtask.model.PaymentRequest;
import com.smartym.testtask.model.PaymentResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Payment {

  @POST("payment-requests")
  Call<PaymentResponse> postPayment(
      @Body PaymentRequest paymentRequest,
      @Header("Authorization") String accessToken,
      @Header("Accept") String accept,
      @Header("Content-Type") String contentType);
}
