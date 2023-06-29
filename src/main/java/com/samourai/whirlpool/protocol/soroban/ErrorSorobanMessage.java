package com.samourai.whirlpool.protocol.soroban;

import com.samourai.soroban.client.AbstractSorobanPayload;

public class ErrorSorobanMessage extends AbstractSorobanPayload {
  public int errorCode;
  public String message;

  public ErrorSorobanMessage() {}

  public ErrorSorobanMessage(int errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }
}
