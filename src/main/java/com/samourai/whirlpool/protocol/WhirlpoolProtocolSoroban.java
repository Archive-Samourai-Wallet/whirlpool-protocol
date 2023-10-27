package com.samourai.whirlpool.protocol;

import com.samourai.soroban.client.RpcWallet;
import com.samourai.wallet.bip47.BIP47UtilGeneric;
import com.samourai.wallet.bip47.rpc.PaymentCode;
import com.samourai.wallet.segwit.SegwitAddress;
import com.samourai.wallet.util.Util;
import com.samourai.whirlpool.client.wallet.beans.WhirlpoolNetwork;
import org.bitcoinj.core.NetworkParameters;

public class WhirlpoolProtocolSoroban {
  private static final String SOROBAN_DIR_REGISTER_INPUT_RESPONSE = "/REGISTER_INPUT_RESPONSE";
  private static final String SOROBAN_DIR_COORDINATORS = "com.samourai.whirlpool.ro.coordinators.";

  public WhirlpoolProtocolSoroban() {}

  public int getRegisterInputFrequencyMs() {
    return 30000;
  }

  public String getDirRegisterInput(WhirlpoolNetwork whirlpoolNetwork, String poolId) {
    return "com.samourai.whirlpool.wo.inputs." + whirlpoolNetwork.name() + "." + poolId;
  }

  public String getDirRegisterInputResponse(
      RpcWallet rpcWalletClient, WhirlpoolNetwork whirlpoolNetwork, BIP47UtilGeneric bip47Util)
      throws Exception {
    return getDirShared(
        rpcWalletClient, whirlpoolNetwork, bip47Util, SOROBAN_DIR_REGISTER_INPUT_RESPONSE);
  }

  public String getDirRegisterInputResponse(
      RpcWallet rpcWalletCoordinator,
      PaymentCode paymentCodeClient,
      BIP47UtilGeneric bip47Util,
      NetworkParameters params)
      throws Exception {
    return getDirShared(
        rpcWalletCoordinator,
        paymentCodeClient,
        bip47Util,
        params,
        SOROBAN_DIR_REGISTER_INPUT_RESPONSE);
  }

  protected String getDirShared(
      RpcWallet rpcWalletClient,
      WhirlpoolNetwork whirlpoolNetwork,
      BIP47UtilGeneric bip47Util,
      String sorobanDir)
      throws Exception {
    SegwitAddress sharedAddress =
        bip47Util.getReceiveAddress(
            rpcWalletClient,
            whirlpoolNetwork.getSigningPaymentCode(),
            0,
            whirlpoolNetwork.getParams());
    return Util.sha512Hex(sharedAddress.getBech32AsString() + "/" + sorobanDir);
  }

  protected String getDirShared(
      RpcWallet rpcWalletCoordinator,
      PaymentCode paymentCodeClient,
      BIP47UtilGeneric bip47Util,
      NetworkParameters params,
      String sorobanDir)
      throws Exception {
    SegwitAddress sharedAddress =
        bip47Util.getSendAddress(rpcWalletCoordinator, paymentCodeClient, 0, params);
    return Util.sha512Hex(sharedAddress.getBech32AsString() + "/" + sorobanDir);
  }

  public String getDirCoordinators(WhirlpoolNetwork whirlpoolNetwork) {
    return SOROBAN_DIR_COORDINATORS + whirlpoolNetwork.name();
  }
}
