package com.samourai.whirlpool.protocol.rest;

@Deprecated // now using PoolInfoSorobanMessage for soroban clients
public class PoolsResponse {
  public PoolInfo[] pools;

  public PoolsResponse() {}

  public PoolsResponse(PoolInfo[] pools) {
    this.pools = pools;
  }
}
