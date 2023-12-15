package com.samourai.whirlpool.protocol.v0.feeOpReturn;

import com.samourai.wallet.bip47.rpc.BIP47Account;
import com.samourai.whirlpool.protocol.v0.feePayload.FeePayloadV1;
import com.samourai.whirlpool.protocol.v0.util.XorMask;
import org.bitcoinj.core.TransactionOutPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// FeePayloadV1_MASKED(64)
public class FeeOpReturnImplV0 extends FeeOpReturnImpl {
  private static final Logger log = LoggerFactory.getLogger(FeeOpReturnImplV0.class);

  public static final short OP_RETURN_VERSION = 0;

  public FeeOpReturnImplV0(XorMask xorMask) {
    super(xorMask, 64, 64, OP_RETURN_VERSION);
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
    byte[] feePayloadMasked = opReturn;
    byte[] signingPublicKey = input0Pubkey;

    // decrypt feePayload
    byte[] feePayload =
        unmaskFeePayload(feePayloadMasked, secretAccountBip47, input0OutPoint, signingPublicKey);
    return new FeeOpReturn(feePayload, opReturnVersion);
  }

  @Override
  public byte[] computeOpReturn(
      String feePaymentCode,
      byte[] feePayload,
      TransactionOutPoint maskingOutpoint,
      byte[] input0PrivKey)
      throws Exception {

    // use input0 key for masking

    // compute feePayloadMasked
    byte[] feePayloadMasked =
        maskFeePayload(feePaymentCode, feePayload, maskingOutpoint, input0PrivKey);

    // compute opReturn
    byte[] opReturn = feePayloadMasked;
    if (opReturn.length != opReturnLength) {
      throw new Exception("Invalid feePayloadMasked.length: " + feePayloadMasked.length);
    }
    return opReturn;
  }

  @Override
  public byte[] computeFeePayload(int feeIndice, short scodePayload, short partner) {
    // always FeePayloadV1
    FeePayloadV1 fp = new FeePayloadV1(feeIndice, scodePayload, partner);
    return fp.computeBytes(this.feePayloadLength);
  }
}
