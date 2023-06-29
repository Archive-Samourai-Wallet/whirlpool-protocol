package com.samourai.whirlpool.protocol;

import com.samourai.soroban.client.RpcWallet;
import com.samourai.wallet.bip47.BIP47UtilGeneric;
import com.samourai.wallet.bip47.rpc.PaymentCode;
import com.samourai.wallet.segwit.SegwitAddress;
import com.samourai.wallet.util.Util;
import com.samourai.wallet.util.Z85;
import com.samourai.whirlpool.client.wallet.beans.WhirlpoolNetwork;
import com.samourai.whirlpool.protocol.beans.Utxo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bitcoinj.core.NetworkParameters;

public class WhirlpoolProtocol {
  /** Current protocol version. */
  public static final String PROTOCOL_VERSION = "0.23";

  // STOMP configuration
  public static final String WS_PREFIX_USER_PRIVATE = "/private";
  public static final String WS_PREFIX_USER_REPLY = "/reply";

  /** Header specifying the message type. */
  public static final String HEADER_MESSAGE_TYPE = "messageType";

  /** Header specifying the protocol version. */
  public static final String HEADER_PROTOCOL_VERSION = "protocolVersion";

  /** Header specifying the pool id. */
  public static final String HEADER_POOL_ID = "poolId";

  public static final String PARTNER_ID_SAMOURAI = "SAMOURAI";

  private static final Z85 z85 = Z85.getInstance();

  public WhirlpoolProtocol() {}

  public static String getUrlCheckOutput(String server) {
    String url = server + WhirlpoolEndpoint.REST_CHECK_OUTPUT;
    return url;
  }

  public static String getUrlRegisterOutput(String server) {
    String url = server + WhirlpoolEndpoint.REST_REGISTER_OUTPUT;
    return url;
  }

  public static String getUrlConnect(String server) {
    server = server.replace("https://", "wss://");
    server = server.replace("http://", "ws://");
    String url = server + WhirlpoolEndpoint.WS_CONNECT;
    return url;
  }

  @Deprecated // pools are now published on Soroban
  public static String getUrlFetchPools(String server) {
    String url = server + WhirlpoolEndpoint.REST_POOLS;
    return url;
  }

  public static String getUrlTx0Data(String server, boolean opReturnV0) {
    String url =
        server
            + (opReturnV0
                ? WhirlpoolEndpoint.REST_TX0_DATA_V0
                : WhirlpoolEndpoint.REST_TX0_DATA_V1);
    return url;
  }

  public static String getUrlTx0Push(String server) {
    String url = server + WhirlpoolEndpoint.REST_TX0_PUSH;
    return url;
  }

  public static int getSorobanRegisterInputFrequencyMs() {
    return 30000;
  }

  public static String getSorobanDirRegisterInput(
      WhirlpoolNetwork whirlpoolNetwork, String poolId) {
    return WhirlpoolEndpoint.SOROBAN_DIR_REGISTER_INPUT_BY_POOL_ID
        + whirlpoolNetwork.name()
        + "."
        + poolId;
  }

  public static String getSorobanDirRegisterInputResponse(
      RpcWallet rpcWalletClient,
      WhirlpoolNetwork whirlpoolNetwork,
      String utxoHash,
      long utxoIndex,
      BIP47UtilGeneric bip47Util,
      NetworkParameters params)
      throws Exception {
    SegwitAddress address =
        bip47Util.getReceiveAddress(
            rpcWalletClient, whirlpoolNetwork.getSigningPaymentCode(), 0, params);
    return getSorobanDirRegisterInputResponse(
        whirlpoolNetwork, address.getBech32AsString(), utxoHash, utxoIndex);
  }

  public static String getSorobanDirRegisterInputResponse(
      RpcWallet rpcWalletCoordinator,
      WhirlpoolNetwork whirlpoolNetwork,
      PaymentCode paymentCodeClient,
      String utxoHash,
      long utxoIndex,
      BIP47UtilGeneric bip47Util,
      NetworkParameters params)
      throws Exception {
    SegwitAddress address =
        bip47Util.getSendAddress(rpcWalletCoordinator, paymentCodeClient, 0, params);
    return getSorobanDirRegisterInputResponse(
        whirlpoolNetwork, address.getBech32AsString(), utxoHash, utxoIndex);
  }

  protected static String getSorobanDirRegisterInputResponse(
      WhirlpoolNetwork whirlpoolNetwork,
      String receiveAddressBech32,
      String utxoHash,
      long utxoIndex) {
    String key =
        whirlpoolNetwork.name() + ":" + receiveAddressBech32 + ":" + utxoHash + ":" + utxoIndex;
    return Util.sha512Hex(key);
  }

  public static String getSorobanDirCoordinators(WhirlpoolNetwork whirlpoolNetwork) {
    return WhirlpoolEndpoint.SOROBAN_DIR_COORDINATORS + whirlpoolNetwork.name();
  }

  public static long computePremixBalanceMin(
      long denomination, long mustMixBalanceMin, boolean liquidity) {
    if (liquidity) {
      return denomination;
    }
    return mustMixBalanceMin;
  }

  public static long computePremixBalanceMax(
      long denomination, long mustMixBalanceMax, boolean liquidity) {
    if (liquidity) {
      return denomination;
    }
    return mustMixBalanceMax;
  }

  public static String computeInputsHash(Collection<Utxo> utxos) {
    List inputs = new ArrayList();
    for (Utxo utxo : utxos) {
      inputs.add(utxo.getHash() + String.valueOf(utxo.getIndex()));
    }
    Collections.sort(inputs);
    String inputsString = joinStrings(";", inputs);
    return Util.sha512Hex(inputsString);
  }

  private static String joinStrings(String delimiter, Collection<String> strings) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for (String str : strings) {
      if (!first) {
        sb.append(delimiter);
      }
      sb.append(str);
      first = false;
    }
    return sb.toString();
  }

  public static byte[] decodeBytes(String encoded) {
    if (encoded == null) {
      return null;
    }
    return z85.decode(encoded);
  }

  public static String encodeBytes(byte[] data) {
    if (data == null) {
      return null;
    }
    return z85.encode(data);
  }
}
