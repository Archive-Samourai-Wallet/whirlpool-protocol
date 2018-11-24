package com.samourai.whirlpool.protocol.util;

import com.samourai.wallet.bip47.rpc.BIP47Account;
import com.samourai.wallet.bip47.rpc.PaymentCode;
import com.samourai.wallet.bip47.rpc.java.SecretPointJava;
import com.samourai.wallet.bip47.rpc.secretPoint.ISecretPoint;
import com.samourai.wallet.hd.HD_Address;
import java.lang.invoke.MethodHandles;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.TransactionOutPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XorMask {
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static XorMask instance = null;

  public static XorMask getInstance() {
    if (instance == null) {
      instance = new XorMask();
    }
    return instance;
  }

  public byte[] mask(
      byte[] dataToMask,
      String paymentCodeOfSecretAccount,
      NetworkParameters params,
      byte[] input0PrivKey,
      TransactionOutPoint input0OutPoint)
      throws Exception {
    HD_Address notifAddressCli =
        new PaymentCode(paymentCodeOfSecretAccount).notificationAddress(params);
    ISecretPoint secretPointMask = new SecretPointJava(input0PrivKey, notifAddressCli.getPubKey());
    byte[] dataMasked = PaymentCode.xorMask(dataToMask, secretPointMask, input0OutPoint);
    return dataMasked;
  }

  public byte[] unmask(
      byte[] dataMasked,
      BIP47Account secretAccount,
      TransactionOutPoint input0OutPoint,
      byte[] input0Pubkey) {
    HD_Address notifAddressServer = secretAccount.getNotificationAddress();
    try {
      ISecretPoint secretPointUnmask =
          new SecretPointJava(notifAddressServer.getECKey().getPrivKeyBytes(), input0Pubkey);
      byte[] dataUnmasked = PaymentCode.xorMask(dataMasked, secretPointUnmask, input0OutPoint);
      return dataUnmasked;
    } catch (Exception e) {
      log.error("", e);
      return null;
    }
  }
}
