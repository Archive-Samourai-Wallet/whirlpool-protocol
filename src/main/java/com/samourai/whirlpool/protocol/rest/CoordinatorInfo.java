package com.samourai.whirlpool.protocol.rest;

public class CoordinatorInfo {
  public String coordinatorId;
  public String urlClear;
  public String urlOnion;

  public CoordinatorInfo() {}

  public CoordinatorInfo(String coordinatorId, String urlClear, String urlOnion) {
    this.coordinatorId = coordinatorId;
    this.urlClear = urlClear;
    this.urlOnion = urlOnion;
  }
}
