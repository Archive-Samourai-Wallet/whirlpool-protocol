package com.samourai.whirlpool.protocol.soroban;

import com.samourai.soroban.client.AbstractSorobanMessageSimple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterInputSorobanMessage extends AbstractSorobanMessageSimple {
  private static final Logger log = LoggerFactory.getLogger(RegisterInputSorobanMessage.class);

  public String poolId;
  public String utxoHash;
  public long utxoIndex;

  public String signature;
  public boolean liquidity;
  public int blockHeight = 0; // zero for protocol < 0.23.9

  public RegisterInputSorobanMessage() {}

  public RegisterInputSorobanMessage(
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
