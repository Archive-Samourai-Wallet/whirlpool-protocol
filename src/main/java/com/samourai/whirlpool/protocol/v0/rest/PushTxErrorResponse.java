package com.samourai.whirlpool.protocol.v0.rest;

import java.util.Collection;

public class PushTxErrorResponse extends RestErrorResponse {
  public static final int ERROR_CODE = 620;
  public String pushTxErrorCode;
  public Collection<Integer> voutsAddressReuse;

  public PushTxErrorResponse() {}

  public PushTxErrorResponse(
      String message, String pushTxErrorCode, Collection<Integer> voutsAddressReuse) {
    super(ERROR_CODE, message);
    this.pushTxErrorCode = pushTxErrorCode;
    this.voutsAddressReuse = voutsAddressReuse;
  }

  public PushTxErrorResponse(String message, String pushTxErrorCode) {
    this(message, pushTxErrorCode, null);
  }

  @Override
  public String toString() {
    return "PushTxErrorResponse{"
        + "pushTxErrorCode='"
        + pushTxErrorCode
        + '\''
        + ", voutsAddressReuse="
        + voutsAddressReuse
        + ", message='"
        + message
        + '\''
        + '}';
  }
}
