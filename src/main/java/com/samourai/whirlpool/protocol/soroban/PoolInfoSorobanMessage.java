package com.samourai.whirlpool.protocol.soroban;

import com.samourai.soroban.client.AbstractSorobanMessageSimple;
import com.samourai.whirlpool.protocol.rest.PoolInfoSoroban;
import java.util.Collection;

public class PoolInfoSorobanMessage extends AbstractSorobanMessageSimple {
  public Collection<PoolInfoSoroban> poolInfo;

  public PoolInfoSorobanMessage() {}

  public PoolInfoSorobanMessage(Collection<PoolInfoSoroban> poolInfo) {
    this.poolInfo = poolInfo;
  }
}
