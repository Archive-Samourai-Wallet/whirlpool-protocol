package com.samourai.whirlpool.protocol.v0.rest;

public class PoolsResponse {
  public PoolInfo[] pools;

  public PoolsResponse() {}

  public PoolsResponse(PoolInfo[] pools) {
    this.pools = pools;
  }
}
