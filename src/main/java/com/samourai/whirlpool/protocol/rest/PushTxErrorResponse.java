package com.samourai.whirlpool.protocol.rest;

import java.util.Collection;

public class PushTxErrorResponse extends RestErrorResponse {
  public String pushTxErrorCode;
  public Collection<Integer> voutsAddressReuse;

  public PushTxErrorResponse() {}

  public PushTxErrorResponse(
      String message, String pushTxErrorCode, Collection<Integer> voutsAddressReuse) {
    super(message);
    this.pushTxErrorCode = pushTxErrorCode;
    this.voutsAddressReuse = voutsAddressReuse;
  }

  public PushTxErrorResponse(String message, String pushTxErrorCode) {
    this(message, pushTxErrorCode, null);
  }
}
