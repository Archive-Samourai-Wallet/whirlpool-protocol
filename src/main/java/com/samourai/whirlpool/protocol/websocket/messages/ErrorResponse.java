package com.samourai.whirlpool.protocol.websocket.messages;

import com.samourai.whirlpool.protocol.websocket.MixMessage;

public class ErrorResponse extends MixMessage {
  public int errorCode;
  public String message;

  public ErrorResponse() {
    super();
  }

  public ErrorResponse(int errorCode, String message) {
    super(null); // no mixId available for errors
    this.errorCode = errorCode;
    this.message = message;
  }
}
