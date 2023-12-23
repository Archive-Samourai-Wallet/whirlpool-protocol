package com.samourai.whirlpool.protocol.v0;

import com.samourai.wallet.util.Z85;
import com.samourai.whirlpool.protocol.v0.beans.Utxo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class WhirlpoolProtocolV0 {
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

  public WhirlpoolProtocolV0() {}

  public static String getUrlCheckOutput(String server) {
    String url = server + WhirlpoolEndpointV0.REST_CHECK_OUTPUT;
    return url;
  }

  public static String getUrlRegisterOutput(String server) {
    String url = server + WhirlpoolEndpointV0.REST_REGISTER_OUTPUT;
    return url;
  }

  public static String getUrlConnect(String server) {
    server = server.replace("https://", "wss://");
    server = server.replace("http://", "ws://");
    String url = server + WhirlpoolEndpointV0.WS_CONNECT;
    return url;
  }

  public static String getUrlFetchPools(String server) {
    String url = server + WhirlpoolEndpointV0.REST_POOLS;
    return url;
  }

  public static String getUrlTx0Data(String server, boolean opReturnV0) {
    String url =
        server
            + (opReturnV0
                ? WhirlpoolEndpointV0.REST_TX0_DATA_V0
                : WhirlpoolEndpointV0.REST_TX0_DATA_V1);
    return url;
  }

  public static String getUrlTx0Push(String server) {
    String url = server + WhirlpoolEndpointV0.REST_TX0_PUSH;
    return url;
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

    // don't use DigestUtils.sha512Hex() for Android compatibility
    String inputsHash = new String(Hex.encodeHex(DigestUtils.sha512(inputsString)));
    return inputsHash;
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
