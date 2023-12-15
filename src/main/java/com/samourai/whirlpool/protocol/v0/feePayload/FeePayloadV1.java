package com.samourai.whirlpool.protocol.v0.feePayload;

import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// used by whirlpool-server + whirlpool-client tests
// feeVersion:short(2) | indice:int(4) | scode:short(2) | feePartner:short(2)
public class FeePayloadV1 {
  private static final Logger log = LoggerFactory.getLogger(FeePayloadV1.class);
  public static final short FEE_PAYLOAD_VERSION = 1;
  private static final int FEE_PAYLOAD_MIN_LENGTH = 10;

  private int feeIndice;
  private short scodePayload;
  private short feePartner;

  public FeePayloadV1(int feeIndice, short scodePayload, short feePartner) {
    this.feeIndice = feeIndice;
    this.scodePayload = scodePayload;
    this.feePartner = feePartner;
  }

  public static FeePayloadV1 parse(byte[] feePayload) throws Exception {
    if (feePayload.length < FEE_PAYLOAD_MIN_LENGTH) {
      throw new Exception(
          "Invalid FeePayload.length = " + feePayload.length + " < " + FEE_PAYLOAD_MIN_LENGTH);
    }
    ByteBuffer bb = ByteBuffer.wrap(feePayload);
    short version = bb.getShort();
    if (version != FEE_PAYLOAD_VERSION) {
      throw new Exception("Invalid FeePayload version: " + version + " vs " + FEE_PAYLOAD_VERSION);
    }
    int feeIndice = bb.getInt();
    short scodePayload = bb.getShort();
    short feePartner = bb.getShort();
    return new FeePayloadV1(feeIndice, scodePayload, feePartner);
  }

  public byte[] computeBytes(int feePayloadLength) {
    ByteBuffer byteBuffer =
        ByteBuffer.allocate(feePayloadLength)
            .putShort(FEE_PAYLOAD_VERSION)
            .putInt(feeIndice)
            .putShort(scodePayload)
            .putShort(feePartner);
    return byteBuffer.array();
  }

  public short getVersion() {
    return FEE_PAYLOAD_VERSION;
  }

  public int getFeeIndice() {
    return feeIndice;
  }

  public short getScodePayload() {
    return scodePayload;
  }

  public short getFeePartner() {
    return feePartner;
  }
}
