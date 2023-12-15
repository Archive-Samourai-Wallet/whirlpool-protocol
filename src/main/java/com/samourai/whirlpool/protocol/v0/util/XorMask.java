package com.samourai.whirlpool.protocol.v0.util;

import com.samourai.wallet.bip47.rpc.BIP47Account;
import com.samourai.wallet.bip47.rpc.PaymentCode;
import com.samourai.wallet.bip47.rpc.secretPoint.ISecretPoint;
import com.samourai.wallet.bip47.rpc.secretPoint.ISecretPointFactory;
import com.samourai.wallet.hd.HD_Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.TransactionOutPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XorMask {
  private static final Logger log = LoggerFactory.getLogger(XorMask.class);
  public static final int MASK_LENGTH = 64;

  private static XorMask instance = null;
  private ISecretPointFactory secretPointFactory;

  public static XorMask getInstance(ISecretPointFactory secretPointFactory) {
    if (instance == null) {
      instance = new XorMask(secretPointFactory);
    }
    return instance;
  }

  private XorMask(ISecretPointFactory secretPointFactory) {
    this.secretPointFactory = secretPointFactory;
  }

  public byte[] mask(
      byte[] dataToMask,
      String paymentCodeOfSecretAccount,
      byte[] maskingPrivKey,
      TransactionOutPoint input0OutPoint)
      throws Exception {
    NetworkParameters params = input0OutPoint.getParams();
    HD_Address notifAddressCli =
        new PaymentCode(paymentCodeOfSecretAccount).notificationAddress(params);
    ISecretPoint secretPointMask =
        secretPointFactory.newSecretPoint(maskingPrivKey, notifAddressCli.getPubKey());
    byte[] dataMasked = PaymentCode.xorMask(dataToMask, secretPointMask, input0OutPoint);
    if (dataMasked == null) {
      throw new Exception("xorMask failed");
    }
    return dataMasked;
  }

  // mask a payload shorter than 64 bytes
  public byte[] maskToLength(
      byte[] dataToMask,
      String paymentCodeOfSecretAccount,
      byte[] maskingPrivKey,
      TransactionOutPoint input0OutPoint)
      throws Exception {
    // mask as 64 bytes
    byte[] dataToMask64 = toMaskLength(dataToMask);
    byte[] dataMasked =
        mask(dataToMask64, paymentCodeOfSecretAccount, maskingPrivKey, input0OutPoint);
    // back to shorter length
    byte[] dataMaskedLength = fromMaskLength(dataMasked, dataToMask.length);
    return dataMaskedLength;
  }

  public byte[] unmask(
      byte[] dataMasked,
      BIP47Account secretAccount,
      TransactionOutPoint input0OutPoint,
      byte[] input0Pubkey)
      throws Exception {
    HD_Address notifAddressServer = secretAccount.getNotificationAddress();

    ISecretPoint secretPointUnmask =
        secretPointFactory.newSecretPoint(
            notifAddressServer.getECKey().getPrivKeyBytes(), input0Pubkey);
    byte[] dataUnmasked = PaymentCode.xorMask(dataMasked, secretPointUnmask, input0OutPoint);
    if (dataUnmasked == null) {
      throw new Exception("xorMask failed");
    }
    return dataUnmasked;
  }

  // unmask a payload shorter than 64 bytes
  public byte[] unmaskToLength(
      byte[] dataMasked,
      BIP47Account secretAccount,
      TransactionOutPoint input0OutPoint,
      byte[] input0Pubkey,
      int dataLength)
      throws Exception {
    if (dataMasked.length > dataLength) {
      throw new Exception("invalid dataMasked.length " + dataMasked.length + " < " + dataLength);
    }

    // unmask as 64 bytes
    byte[] dataMasked64 = toMaskLength(dataMasked);
    byte[] dataUnmasked = unmask(dataMasked64, secretAccount, input0OutPoint, input0Pubkey);
    // back to shorter length
    return fromMaskLength(dataUnmasked, dataLength);
  }

  public byte[] toMaskLength(byte[] payload) throws Exception {
    if (payload.length == MASK_LENGTH) {
      return payload;
    }
    if (payload.length > MASK_LENGTH) {
      throw new Exception("Invalid payload.length=" + payload.length + " > " + MASK_LENGTH);
    }
    // FEE_PAYLOAD_LENGTH => MASK_LENGTH (filled with zeros)
    byte[] mask64 = new byte[MASK_LENGTH]; // we need 64 bytes for xorMask()
    System.arraycopy(payload, 0, mask64, 0, payload.length);
    return mask64;
  }

  // MASK_LENGTH => FEE_PAYLOAD_LENGTH (trimmed ending zeros)
  public byte[] fromMaskLength(byte[] mask64, int payloadLength) throws Exception {
    if (payloadLength == mask64.length) {
      return mask64;
    }
    if (payloadLength > MASK_LENGTH) {
      throw new Exception("Invalid payloadLength=" + payloadLength + " vs " + MASK_LENGTH);
    }
    byte[] payload = new byte[payloadLength]; // we need 64 bytes for xorMask()
    System.arraycopy(mask64, 0, payload, 0, payloadLength);
    return payload;
  }
}
