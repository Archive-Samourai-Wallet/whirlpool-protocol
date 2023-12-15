package com.samourai.whirlpool.protocol.v0.feeOpReturn;

import com.samourai.wallet.bip47.rpc.BIP47Account;
import com.samourai.whirlpool.protocol.v0.feePayload.FeePayloadV1;
import com.samourai.whirlpool.protocol.v0.util.XorMask;
import java.math.BigInteger;
import java.util.Arrays;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.TransactionOutPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// FeePayloadV1_MASKED(46) | maskingPublicKey(33) | OP_RETURN_VERSION(1)
public class FeeOpReturnImplV1 extends FeeOpReturnImpl {
  private static final Logger log = LoggerFactory.getLogger(FeeOpReturnImplV1.class);

  public static final short OP_RETURN_VERSION = 1;
  protected static final int SIGNING_KEY_LENGTH = 33;

  public FeeOpReturnImplV1(XorMask xorMask) {
    super(xorMask, 80, 80 - SIGNING_KEY_LENGTH - OP_RETURN_VERSION_LENGTH, OP_RETURN_VERSION);
  }

  protected int getSigningPublicKeyOffset() {
    return opReturnLength - SIGNING_KEY_LENGTH - OP_RETURN_VERSION_LENGTH;
  }

  @Override
  public FeeOpReturn parseOpReturn(
      byte[] opReturn,
      BIP47Account secretAccountBip47,
      TransactionOutPoint input0OutPoint,
      byte[] input0Pubkey)
      throws Exception {
    if (!acceptsOpReturn(opReturn)) {
      throw new Exception("Invalid opReturn.length=" + opReturn.length + " vs " + opReturnLength);
    }

    // parse opReturn
    byte[] feePayloadMasked = extractFeePayloadMasked(opReturn);
    byte[] signingPublicKey = extractSigningPublicKey(opReturn);

    // decrypt feePayload
    byte[] feePayload =
        unmaskFeePayload(feePayloadMasked, secretAccountBip47, input0OutPoint, signingPublicKey);
    return new FeeOpReturn(feePayload, opReturnVersion);
  }

  protected byte[] extractFeePayloadMasked(byte[] opReturn) {
    byte[] feePayloadMasked = Arrays.copyOfRange(opReturn, 0, feePayloadLength);
    return feePayloadMasked;
  }

  protected byte[] extractSigningPublicKey(byte[] opReturn) {
    byte[] signingPublicKey =
        Arrays.copyOfRange(
            opReturn,
            getSigningPublicKeyOffset(),
            getSigningPublicKeyOffset() + SIGNING_KEY_LENGTH);
    return signingPublicKey;
  }

  protected byte[] generateMaskingPrivKey() {
    if (isTestMode()) {
      // static key for unit tests
      return ECKey.fromPrivate(
              new BigInteger(
                  "34069012401142361066035129995856280497224474312925604298733347744482107649210"))
          .getPrivKeyBytes();
    }
    return new ECKey().getPrivKeyBytes();
  }

  @Override
  public byte[] computeOpReturn(
      String feePaymentCode,
      byte[] feePayload,
      TransactionOutPoint maskingOutpoint,
      byte[] input0PrivKey) // we dont use input0PrivKey anymore
      throws Exception {

    // use temporary key for masking
    byte[] maskingPrivKey = generateMaskingPrivKey();

    // compute feePayloadMasked
    byte[] feePayloadMasked =
        maskFeePayload(feePaymentCode, feePayload, maskingOutpoint, maskingPrivKey);

    // compute opReturn
    byte[] maskingPublicKey = ECKey.fromPrivate(maskingPrivKey).getPubKey();
    byte[] opReturn = new byte[opReturnLength];
    System.arraycopy(feePayloadMasked, 0, opReturn, 0, feePayloadMasked.length);
    System.arraycopy(
        maskingPublicKey, 0, opReturn, getSigningPublicKeyOffset(), SIGNING_KEY_LENGTH);
    System.arraycopy(
        new byte[] {getOpReturnVersionByte()},
        0,
        opReturn,
        getOpReturnVersionOffset(),
        OP_RETURN_VERSION_LENGTH);
    return opReturn;
  }

  @Override
  public byte[] computeFeePayload(int feeIndice, short scodePayload, short partner) {
    // always FeePayloadV1
    FeePayloadV1 fp = new FeePayloadV1(feeIndice, scodePayload, partner);
    return fp.computeBytes(this.feePayloadLength);
  }
}
