package com.samourai.whirlpool.protocol;

import com.samourai.soroban.client.RpcWallet;
import com.samourai.soroban.client.RpcWalletImpl;
import com.samourai.wallet.bip47.BIP47UtilGeneric;
import com.samourai.wallet.bip47.rpc.BIP47Wallet;
import com.samourai.wallet.bip47.rpc.PaymentCode;
import com.samourai.wallet.bip47.rpc.java.Bip47UtilJava;
import com.samourai.wallet.crypto.CryptoUtil;
import com.samourai.wallet.hd.HD_WalletFactoryGeneric;
import com.samourai.whirlpool.protocol.beans.Utxo;
import java.util.LinkedList;
import java.util.List;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.TestNet3Params;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WhirlpoolProtocolTest {
  protected static final String SEED_WORDS = "all all all all all all all all all all all all";
  protected static final String SEED_PASSPHRASE = "whirlpool";

  protected static final NetworkParameters params = TestNet3Params.get();
  protected static final HD_WalletFactoryGeneric hdWalletFactory =
      HD_WalletFactoryGeneric.getInstance();
  protected static final BIP47UtilGeneric bip47Util = Bip47UtilJava.getInstance();
  protected static final CryptoUtil cryptoUtil = CryptoUtil.getInstanceJava();

  protected RpcWallet rpcWalletCoordinator;
  protected RpcWallet rpcWalletClient;
  protected BIP47Wallet bip47WalletCoordinator;
  protected BIP47Wallet bip47WalletClient;
  protected PaymentCode paymentCodeCoordinator;
  protected PaymentCode paymentCodeClient;

  @BeforeEach
  public void setUp() throws Exception {
    byte[] seed = hdWalletFactory.computeSeedFromWords(SEED_WORDS);

    bip47WalletCoordinator =
        new BIP47Wallet(hdWalletFactory.getBIP44(seed, SEED_PASSPHRASE + "coordinator", params));
    bip47WalletClient =
        new BIP47Wallet(hdWalletFactory.getBIP44(seed, SEED_PASSPHRASE + "client", params));

    paymentCodeCoordinator = bip47Util.getPaymentCode(bip47WalletCoordinator);
    paymentCodeClient = bip47Util.getPaymentCode(bip47WalletClient);

    rpcWalletCoordinator = new RpcWalletImpl(bip47WalletCoordinator, cryptoUtil);
    rpcWalletClient = new RpcWalletImpl(bip47WalletClient, cryptoUtil);
  }

  @Test
  public void computeInputsHash() throws Exception {
    List<Utxo> utxos = new LinkedList<>();

    utxos.add(new Utxo("040df121854c7db49e38b6fcb61c2b0953c8b234ce53c1b2a2fb122a4e1c3d2e", 0));
    Assertions.assertEquals(
        "d98762e7d256e9cbc93d6cf9f4ff160c6a3253e8665d34ab6321b0220bc00c9393ba6f6008b75b8ca89a37514c1fcda8a4ad01069af8488ea86357ff219a25bd",
        WhirlpoolProtocol.computeInputsHash(utxos));

    utxos.add(new Utxo("6517ece36402a89d76d075c60a8d3d0e051e4e5efa42a01c9033328707631b61", 5));
    Assertions.assertEquals(
        "a8615d928839ab1a23c7a32f2f58cfa5af92bfc46469d3acf42df75f23b731cc4a8fccd94bf65477c887c5b8705e4ef86574362dc9244ae5d4d5835b6eb3a357",
        WhirlpoolProtocol.computeInputsHash(utxos));
  }

  @Test
  public void getSorobanDirInviteMixSend() throws Exception {
    String utxoHash = "6517ece36402a89d76d075c60a8d3d0e051e4e5efa42a01c9033328707631b61";
    long utxoIndex = 3;
    String directory =
        WhirlpoolProtocol.getSorobanDirInviteMixSend(
            rpcWalletCoordinator, paymentCodeClient, utxoHash, utxoIndex, bip47Util, params);
    Assertions.assertEquals(
        "6f7a4bac5fab607167b63955d778066074cfcf5cc34028f7b712e5d34012ab81b5febbf9434b8ce519c69487978c9e47b1b5967f3c992fbad40f650cda624964",
        directory);
  }

  @Test
  public void getSorobanDirInviteMixReceive() throws Exception {
    String utxoHash = "6517ece36402a89d76d075c60a8d3d0e051e4e5efa42a01c9033328707631b61";
    long utxoIndex = 3;
    String directory =
        WhirlpoolProtocol.getSorobanDirInviteMixReceive(
            rpcWalletClient, paymentCodeCoordinator, utxoHash, utxoIndex, bip47Util, params);
    Assertions.assertEquals(
        "6f7a4bac5fab607167b63955d778066074cfcf5cc34028f7b712e5d34012ab81b5febbf9434b8ce519c69487978c9e47b1b5967f3c992fbad40f650cda624964",
        directory);
  }
}
