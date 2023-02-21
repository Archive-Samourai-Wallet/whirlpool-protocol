package com.samourai.whirlpool.protocol.websocket.messages;

public class ConfirmInputRequest {
  public String mixId;
  public String blindedBordereau64;
  public String userHash;
  public String utxoHash; // null for non-soroban inputs
  public long utxoIndex; // null for non-soroban inputs

  public ConfirmInputRequest() {}

  public ConfirmInputRequest(
      String mixId, String blindedBordereau64, String userHash, String utxoHash, long utxoIndex) {
    this.mixId = mixId;
    this.blindedBordereau64 = blindedBordereau64;
    this.userHash = userHash;
    this.utxoHash = utxoHash;
    this.utxoIndex = utxoIndex;
  }
}
