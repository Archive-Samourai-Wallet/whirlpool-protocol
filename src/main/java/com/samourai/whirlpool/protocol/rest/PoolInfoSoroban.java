package com.samourai.whirlpool.protocol.rest;

public class PoolInfoSoroban {
  public String poolId;
  public long denomination;
  public long feeValue;
  public long premixValue;
  public long premixValueMin;
  public long premixValueMax;
  public int tx0MaxOutputs;
  public int anonymitySet;

  public PoolInfoSoroban() {}

  public PoolInfoSoroban(
      String poolId,
      long denomination,
      long feeValue,
      long premixValue,
      long premixValueMin,
      long premixValueMax,
      int tx0MaxOutputs,
      int anonymitySet) {
    this.poolId = poolId;
    this.denomination = denomination;
    this.feeValue = feeValue;
    this.premixValue = premixValue;
    this.premixValueMin = premixValueMin;
    this.premixValueMax = premixValueMax;
    this.tx0MaxOutputs = tx0MaxOutputs;
    this.anonymitySet = anonymitySet;
  }
}
