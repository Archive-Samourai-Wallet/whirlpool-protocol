package com.samourai.whirlpool.protocol.v0.rest;

public class RegisterOutputRequest {
  public String inputsHash;
  public String unblindedSignedBordereau64;
  public String receiveAddress;
  public String bordereau64; // since 0.23.9 (before, it was based on receiveAddress)

  public RegisterOutputRequest() {}

  public RegisterOutputRequest(
      String inputsHash,
      String unblindedSignedBordereau64,
      String receiveAddress,
      String bordereau64) {
    this.inputsHash = inputsHash;
    this.unblindedSignedBordereau64 = unblindedSignedBordereau64;
    this.receiveAddress = receiveAddress;
    this.bordereau64 = bordereau64;
  }
}
