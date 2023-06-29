package com.samourai.whirlpool.protocol.soroban;

import com.samourai.soroban.client.AbstractSorobanPayload;
import com.samourai.whirlpool.protocol.rest.CoordinatorInfo;
import com.samourai.whirlpool.protocol.rest.PoolInfoSoroban;
import java.util.Collection;

public class RegisterCoordinatorSorobanMessage extends AbstractSorobanPayload {
  public CoordinatorInfo coordinator;
  public Collection<PoolInfoSoroban> pools;

  public RegisterCoordinatorSorobanMessage() {}

  public RegisterCoordinatorSorobanMessage(
      CoordinatorInfo coordinator, Collection<PoolInfoSoroban> pools) {
    this.coordinator = coordinator;
    this.pools = pools;
  }
}
