package com.samourai.whirlpool.protocol.v0.feeOpReturn;

import com.samourai.wallet.bip47.rpc.BIP47Account;
import com.samourai.whirlpool.protocol.v0.util.XorMask;
import org.bitcoinj.core.TransactionOutPoint;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FeeOpReturnImpl {
  private static final Logger log = LoggerFactory.getLogger(FeeOpReturnImpl.class);

  protected XorMask xorMask;
  protected int opReturnLength;
  protected int feePayloadLength;
  protected short opReturnVersion;
  private boolean testMode; // true for unit tests
  protected static int OP_RETURN_VERSION_LENGTH = 1;

  public FeeOpReturnImpl(
      XorMask xorMask, int opReturnLength, int feePayloadLength, short opReturnVersion) {
    this.xorMask = xorMask;
    this.opReturnLength = opReturnLength;
    this.feePayloadLength = feePayloadLength;
    this.opReturnVersion = opReturnVersion;
    this.testMode = false;
  }

  public boolean acceptsOpReturn(byte[] opReturn) {
    return opReturn.length == opReturnLength
        // no OP_RETURN_VERSION check for V0
        && (opReturnVersion == 0 || opReturn[opReturn.length - 1] == getOpReturnVersionByte());
  }

  public boolean acceptsFeePayload(byte[] feePayload) {
    return feePayload.length == feePayloadLength;
  }

  protected byte getOpReturnVersionByte() {
    return Short.valueOf(opReturnVersion).byteValue();
  }

  protected int getOpReturnVersionOffset() {
    return opReturnLength - 1;
  }

  protected byte[] unmaskFeePayload(
      byte[] feePayloadMasked,
      BIP47Account secretAccountBip47,
      TransactionOutPoint input0OutPoint,
      byte[] signingKey)
      throws Exception {
    if (!acceptsFeePayload(feePayloadMasked)) {
      log.error(
          "Invalid feePayloadMasked.length: " + feePayloadMasked + " vs " + this.feePayloadLength);
      return null;
    }
    // decrypt feePayload
    byte[] feePayload =
        xorMask.unmaskToLength(
            feePayloadMasked, secretAccountBip47, input0OutPoint, signingKey, feePayloadLength);
    return feePayload;
  }

  protected byte[] maskFeePayload(
      String feePaymentCode,
      byte[] feePayload,
      TransactionOutPoint maskingOutpoint,
      byte[] maskingPrivKey)
      throws Exception {
    if (log.isDebugEnabled()) {
      log.debug("feePayloadHex=" + Hex.toHexString(feePayload));
    }
    if (!acceptsFeePayload(feePayload)) {
      throw new Exception(
          "Invalid feePayload.length: " + feePayload.length + " vs " + this.feePayloadLength);
    }
    byte[] feePayloadMasked =
        xorMask.maskToLength(feePayload, feePaymentCode, maskingPrivKey, maskingOutpoint);
    if (!acceptsFeePayload(feePayloadMasked)) {
      throw new Exception(
          "Invalid feePayloadMasked.length: "
              + feePayloadMasked.length
              + " vs "
              + this.feePayloadLength);
    }
    return feePayloadMasked;
  }

  public abstract FeeOpReturn parseOpReturn(
      byte[] opReturn,
      BIP47Account secretAccountBip47,
      TransactionOutPoint input0OutPoint,
      byte[] input0Pubkey)
      throws Exception;

  public abstract byte[] computeFeePayload(int feeIndice, short scodePayload, short partner);

  public abstract byte[] computeOpReturn(
      String feePaymentCode,
      byte[] feePayload,
      TransactionOutPoint maskingOutpoint,
      byte[] input0PrivKey)
      throws Exception;

  public short getOpReturnVersion() {
    return opReturnVersion;
  }

  public void setTestMode(boolean testMode) {
    this.testMode = testMode;
  }

  public boolean isTestMode() {
    return testMode;
  }
}
