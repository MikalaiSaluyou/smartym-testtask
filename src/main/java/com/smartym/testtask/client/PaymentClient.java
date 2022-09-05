package com.smartym.testtask.client;

import com.smartym.testtask.exception.PaymentException;
import com.smartym.testtask.model.PaymentRequest;
import com.smartym.testtask.model.PaymentResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

@Service
public class PaymentClient {
  private final Retrofit retrofit;

  @Autowired
  public PaymentClient(Retrofit retrofit) {
    this.retrofit = retrofit;
  }

  public PaymentResponse postPayment(final PaymentRequest request, final String accessToken)
      throws IOException {
    final Payment payment = retrofit.create(Payment.class);

    final Call<PaymentResponse> retrofitCall =
        payment.postPayment(
            request, accessToken, MediaType.ALL_VALUE, MediaType.APPLICATION_JSON_VALUE);

    final Response<PaymentResponse> response = retrofitCall.execute();

    if (!response.isSuccessful()) {
      throw new PaymentException(
          response.errorBody() != null
              ? response.errorBody().string()
              : "Unknown error while submitting the payment");
    }
    return response.body();
  }
}
