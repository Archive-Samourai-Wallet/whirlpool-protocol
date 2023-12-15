package com.samourai.whirlpool.protocol.v0.feeOpReturn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeeOpReturn {
  private static final Logger log = LoggerFactory.getLogger(FeeOpReturn.class);

  private byte[] feePayload;
  private short opReturnVersion;

  public FeeOpReturn(byte[] feePayload, short opReturnVersion) {
    this.feePayload = feePayload;
    this.opReturnVersion = opReturnVersion;
  }

  public byte[] getFeePayload() {
    return feePayload;
  }

  public short getOpReturnVersion() {
    return opReturnVersion;
  }
}
