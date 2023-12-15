package com.samourai.whirlpool.protocol.v0.rest;

public class RestErrorResponse {
  private int errorCode; // TODO enable on next release
  public String message;

  public RestErrorResponse() {}

  public RestErrorResponse(int errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

  @Override
  public String toString() {
    return "RestErrorResponse{" + "errorCode=" + errorCode + ", message='" + message + '\'' + '}';
  }
}
