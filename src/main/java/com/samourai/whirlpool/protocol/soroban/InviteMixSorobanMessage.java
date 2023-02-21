package com.samourai.whirlpool.protocol.soroban;

import com.samourai.soroban.client.AbstractSorobanMessageSimple;

public class InviteMixSorobanMessage extends AbstractSorobanMessageSimple {
  public String mixId;
  public byte[] mixPublicKey;
  public String coordinatorIp;

  public InviteMixSorobanMessage() {}

  public InviteMixSorobanMessage(String mixId, byte[] mixPublicKey, String coordinatorIp) {
    this.mixId = mixId;
    this.mixPublicKey = mixPublicKey;
    this.coordinatorIp = coordinatorIp;
  }
}
