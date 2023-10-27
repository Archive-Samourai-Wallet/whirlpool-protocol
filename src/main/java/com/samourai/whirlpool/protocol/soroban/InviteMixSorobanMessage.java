package com.samourai.whirlpool.protocol.soroban;

import com.samourai.soroban.client.AbstractSorobanPayload;

public class InviteMixSorobanMessage extends AbstractSorobanPayload {
  public String mixId;
  public byte[] mixPublicKey;
  public String coordinatorUrlClear;
  public String coordinatorUrlOnion;

  public InviteMixSorobanMessage() {}

  public InviteMixSorobanMessage(
      String mixId, byte[] mixPublicKey, String coordinatorUrlClear, String coordinatorUrlOnion) {
    this.mixId = mixId;
    this.mixPublicKey = mixPublicKey;
    this.coordinatorUrlClear = coordinatorUrlClear;
    this.coordinatorUrlOnion = coordinatorUrlOnion;
  }
}
