package com.samourai.whirlpool.protocol.websocket.messages;

public class RegisterInputRequest {
  public String poolId;
  public String utxoHash;
  public long utxoIndex;

  public String signature;
  public boolean liquidity;
  public int blockHeight = 0; // zero for protocol < 0.23.9

  public RegisterInputRequest() {}

  public RegisterInputRequest(
      String poolId,
      String utxoHash,
      long utxoIndex,
      String signature,
      boolean liquidity,
      int blockHeight) {
    this.poolId = poolId;
    this.utxoHash = utxoHash;
    this.utxoIndex = utxoIndex;
    this.signature = signature;
    this.liquidity = liquidity;
    this.blockHeight = blockHeight;
  }
}
