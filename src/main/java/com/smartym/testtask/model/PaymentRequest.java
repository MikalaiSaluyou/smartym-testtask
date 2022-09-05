package com.smartym.testtask.model;

import java.math.BigDecimal;
import java.time.Instant;

public class PaymentRequest {
  private String creditorIban;
  private String creditorName;
  private BigDecimal amount;
  private String currency;
  private String description;
  private Instant creationDateTime;

  public String getCreditorIban() {
    return creditorIban;
  }

  public void setCreditorIban(String creditorIban) {
    this.creditorIban = creditorIban;
  }

  public String getCreditorName() {
    return creditorName;
  }

  public void setCreditorName(String creditorName) {
    this.creditorName = creditorName;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Instant getCreationDateTime() {
    return creationDateTime;
  }

  public void setCreationDateTime(Instant creationDateTime) {
    this.creationDateTime = creationDateTime;
  }
}
