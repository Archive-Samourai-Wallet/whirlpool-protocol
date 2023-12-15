package com.samourai.whirlpool.protocol.v0.rest;

public class PushTxSuccessResponse {
  public String txid;

  public PushTxSuccessResponse() {}

  public PushTxSuccessResponse(String txid) {
    this.txid = txid;
  }
}
