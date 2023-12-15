package com.samourai.whirlpool.protocol.v0;

public class WhirlpoolEndpointV0 {

  // WEBSOCKET endpoints
  public static final String WS_PREFIX = "/ws/";
  public static final String WS_CONNECT = WS_PREFIX + "connect";
  public static final String WS_REGISTER_INPUT = WS_PREFIX + "registerInput";
  public static final String WS_CONFIRM_INPUT = WS_PREFIX + "confirmInput";
  public static final String WS_REVEAL_OUTPUT = WS_PREFIX + "revealOutput";
  public static final String WS_SIGNING = WS_PREFIX + "signing";

  // REST endpoints
  public static final String REST_PREFIX = "/rest/";
  public static final String REST_CHECK_OUTPUT = REST_PREFIX + "checkOutput";
  public static final String REST_REGISTER_OUTPUT = REST_PREFIX + "registerOutput";
  public static final String REST_POOLS = REST_PREFIX + "pools";
  public static final String REST_TX0_DATA_V0 = REST_PREFIX + "tx0";
  public static final String REST_TX0_DATA_V1 = REST_PREFIX + "tx0/v1";
  public static final String REST_TX0_PUSH = REST_PREFIX + "tx0/push";

  public WhirlpoolEndpointV0() {}
}
