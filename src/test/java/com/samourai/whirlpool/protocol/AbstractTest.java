package com.samourai.whirlpool.protocol;

import com.samourai.wallet.bip47.rpc.BIP47Account;
import com.samourai.wallet.bip47.rpc.BIP47Wallet;
import com.samourai.wallet.bip47.rpc.java.SecretPointFactoryJava;
import com.samourai.wallet.hd.HD_Wallet;
import com.samourai.wallet.hd.HD_WalletFactoryGeneric;
import com.samourai.wallet.segwit.SegwitAddress;
import com.samourai.wallet.segwit.bech32.Bech32UtilGeneric;
import com.samourai.wallet.util.CryptoTestUtil;
import com.samourai.whirlpool.protocol.util.XorMask;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.params.TestNet3Params;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTest {
  protected static final Logger log = LoggerFactory.getLogger(AbstractTest.class);
  protected static XorMask xorMask = XorMask.getInstance(SecretPointFactoryJava.getInstance());
  protected NetworkParameters params = TestNet3Params.get();
  protected Bech32UtilGeneric bech32Util = Bech32UtilGeneric.getInstance();
  protected HD_WalletFactoryGeneric hdWalletFactory = HD_WalletFactoryGeneric.getInstance();
  protected static final CryptoTestUtil cryptoTestUtil = CryptoTestUtil.getInstance();

  protected String SEED_WORDS = "all all all all all all all all all all all all";
  protected String SEED_PASSPHRASE = "test";
  private String PCODE =
      "PM8TJgqKqMesJ52RveEtE6qLKjKqWVB4dMormiywD2hwAGd3MiGxvurnFC6jMFS4hPa2Xb9JUWJYryty5Q9osQ3ELxgGpVMrERpFbNXfe9Th3nAwHfag";

  protected static String bytesToBinaryString(byte[] bytes) {
    List<String> strs = new ArrayList<>();
    for (byte b : bytes) {
      String str = String.format("%8s", Integer.toBinaryString((b + 256) % 256)).replace(' ', '0');
      strs.add(str);
    }
    return StringUtils.join(strs.toArray(), " ");
  }

  protected static byte[] bytesFromBinaryString(String str) {
    String[] bytesStrs = str.split(" ");
    byte[] result = new byte[bytesStrs.length];
    for (int i = 0; i < bytesStrs.length; i++) {
      String byteStr = bytesStrs[i];
      result[i] = (byte) (int) (Integer.valueOf(byteStr, 2));
    }
    return result;
  }

  public TransactionOutput mockTxOutput(SegwitAddress address) throws Exception {
    // generate transaction with bitcoinj
    Transaction transaction = new Transaction(params);

    // add outputs
    String addressBech32 = address.getBech32AsString();
    TransactionOutput transactionOutput =
        bech32Util.getTransactionOutput(addressBech32, 1234, params);
    transaction.addOutput(transactionOutput);

    // add coinbase input
    int txCounter = 1;
    TransactionInput transactionInput =
        new TransactionInput(
            params, transaction, new byte[] {(byte) txCounter, (byte) (txCounter++ >> 8)});
    transaction.addInput(transactionInput);
    return transactionOutput;
  }

  protected BIP47Account computeBip47Account() throws Exception {
    return computeBip47Account(SEED_WORDS, SEED_PASSPHRASE, PCODE);
  }

  protected BIP47Account computeBip47Account(String seed, String passphrase, String pcode)
      throws Exception {
    HD_Wallet bip44wallet = hdWalletFactory.restoreWallet(seed, passphrase, params);
    BIP47Wallet bip47Wallet = new BIP47Wallet(bip44wallet);
    BIP47Account bip47Account = bip47Wallet.getAccount(0);
    Assertions.assertEquals(pcode, bip47Account.getPaymentCode());
    return bip47Account;
  }
}
