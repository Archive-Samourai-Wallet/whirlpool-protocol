package com.samourai.whirlpool.protocol;

import com.samourai.wallet.util.Util;
import com.samourai.wallet.util.Z85;
import com.samourai.whirlpool.protocol.beans.Utxo;
import java.util.*;

public class WhirlpoolProtocol {
  public static final String PROTOCOL_VERSION = "0.23";
  public static final String WS_PREFIX_USER_PRIVATE = "/private";
  public static final String WS_PREFIX_USER_REPLY = "/reply";
  public static final String HEADER_MESSAGE_TYPE = "messageType";
  public static final String HEADER_PROTOCOL_VERSION = "protocolVersion";
  public static final String HEADER_POOL_ID = "poolId";
  public static final String PARTNER_ID_SAMOURAI = "SAMOURAI";
  private static final Z85 z85 = Z85.getInstance();

  public WhirlpoolProtocol() {}

  public static String getUrlCheckOutput(String server) {
    String url = server + "/rest/checkOutput";
    return url;
  }

  public static String getUrlRegisterOutput(String server) {
    String url = server + "/rest/registerOutput";
    return url;
  }

  public static String getUrlConnect(String server) {
    server = server.replace("https://", "wss://");
    server = server.replace("http://", "ws://");
    String url = server + "/ws/connect";
    return url;
  }

  /** @deprecated */
  @Deprecated
  public static String getUrlFetchPools(String server) {
    String url = server + "/rest/pools";
    return url;
  }

  public static String getUrlTx0Data(String server, boolean opReturnV0) {
    String url = server + (opReturnV0 ? "/rest/tx0" : "/rest/tx0/v1");
    return url;
  }

  public static String getUrlTx0Push(String server) {
    String url = server + "/rest/tx0/push";
    return url;
  }

  public static long computePremixBalanceMin(
      long denomination, long mustMixBalanceMin, boolean liquidity) {
    return liquidity ? denomination : mustMixBalanceMin;
  }

  public static long computePremixBalanceMax(
      long denomination, long mustMixBalanceMax, boolean liquidity) {
    return liquidity ? denomination : mustMixBalanceMax;
  }

  public static String computeInputsHash(Collection<Utxo> utxos) {
    List inputs = new ArrayList();
    Iterator var2 = utxos.iterator();

    while (var2.hasNext()) {
      Utxo utxo = (Utxo) var2.next();
      inputs.add(utxo.getHash() + String.valueOf(utxo.getIndex()));
    }

    Collections.sort(inputs);
    String inputsString = joinStrings(";", inputs);
    return Util.sha512Hex(inputsString);
  }

  private static String joinStrings(String delimiter, Collection<String> strings) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;

    for (Iterator var4 = strings.iterator(); var4.hasNext(); first = false) {
      String str = (String) var4.next();
      if (!first) {
        sb.append(delimiter);
      }

      sb.append(str);
    }

    return sb.toString();
  }

  public static byte[] decodeBytes(String encoded) {
    return encoded == null ? null : z85.decode(encoded);
  }

  public static String encodeBytes(byte[] data) {
    return data == null ? null : z85.encode(data);
  }
}
