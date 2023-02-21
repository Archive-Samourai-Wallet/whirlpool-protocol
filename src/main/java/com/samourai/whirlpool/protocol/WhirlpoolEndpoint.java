package com.samourai.whirlpool.protocol;

public class WhirlpoolEndpoint {

  // WEBSOCKET endpoints
  public static final String WS_PREFIX = "/ws/";
  public static final String WS_CONNECT = WS_PREFIX + "connect";
  @Deprecated public static final String WS_REGISTER_INPUT = WS_PREFIX + "registerInput";
  public static final String WS_CONFIRM_INPUT = WS_PREFIX + "confirmInput";
  public static final String WS_REVEAL_OUTPUT = WS_PREFIX + "revealOutput";
  public static final String WS_SIGNING = WS_PREFIX + "signing";

  // REST endpoints
  public static final String REST_PREFIX = "/rest/";
  public static final String REST_CHECK_OUTPUT = REST_PREFIX + "checkOutput";
  public static final String REST_REGISTER_OUTPUT = REST_PREFIX + "registerOutput";

  @Deprecated // pools are now published on Soroban
  public static final String REST_POOLS = REST_PREFIX + "pools";
  public static final String REST_TX0_DATA_V0 = REST_PREFIX + "tx0";
  public static final String REST_TX0_DATA_V1 = REST_PREFIX + "tx0/v1";
  public static final String REST_TX0_PUSH = REST_PREFIX + "tx0/push";

  // SOROBAN directories
  public static final String SOROBAN_DIR_REGISTER_INPUT_BY_POOL_ID =
      "com.samourai.whirlpool.wo.REGISTER_INPUT.";
  public static final String SOROBAN_DIR_POOLS = "com.samourai.whirlpool.ro.pools";

  public WhirlpoolEndpoint() {}
}
