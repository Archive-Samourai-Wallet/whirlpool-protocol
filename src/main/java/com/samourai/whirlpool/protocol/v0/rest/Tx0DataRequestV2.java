package com.samourai.whirlpool.protocol.v0.rest;

public class Tx0DataRequestV2 {
  public String scode;
  public String partnerId;
  public boolean cascading;

  public Tx0DataRequestV2() {}

  public Tx0DataRequestV2(String scode, String partnerId, boolean cascading) {
    this.scode = scode;
    this.partnerId = partnerId;
    this.cascading = cascading;
  }
}
