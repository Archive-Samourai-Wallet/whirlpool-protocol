package com.samourai.whirlpool.protocol.rest;

public class Tx0NotifyRequest {
  public String txid;

  public Tx0NotifyRequest() {}

  public Tx0NotifyRequest(String txid) {
    this.txid = txid;
  }
}
