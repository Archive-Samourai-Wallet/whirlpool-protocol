package com.samourai.whirlpool.protocol.v0.beans;

public class Utxo {
  private String hash;
  private long index;

  public Utxo(String hash, long index) {
    this.hash = hash;
    this.index = index;
  }

  public String getHash() {
    return hash;
  }

  public long getIndex() {
    return index;
  }
}
