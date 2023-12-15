package com.samourai.whirlpool.protocol.v0.rest;

public class Tx0PushRequest {
  public String tx64;
  public String poolId;

  public Tx0PushRequest() {}

  public Tx0PushRequest(String tx64, String poolId) {
    this.tx64 = tx64;
    this.poolId = poolId;
  }
}
