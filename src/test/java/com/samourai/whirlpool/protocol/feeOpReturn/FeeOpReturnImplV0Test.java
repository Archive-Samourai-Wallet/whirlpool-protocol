package com.samourai.whirlpool.protocol.feeOpReturn;

import com.samourai.wallet.bip47.rpc.BIP47Account;
import com.samourai.wallet.segwit.SegwitAddress;
import com.samourai.whirlpool.protocol.AbstractTest;
import com.samourai.whirlpool.protocol.feePayload.FeePayloadV1;
import java.math.BigInteger;
import org.bitcoinj.core.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FeeOpReturnImplV0Test extends AbstractTest {
  private static String SEED_WORDS_V0 = "all all all all all all all all all all all all";
  private static String SEED_PASSPHRASE_V0 = "whirlpool";
  private static String PCODE_V0 =
      "PM8TJXp19gCE6hQzqRi719FGJzF6AreRwvoQKLRnQ7dpgaakakFns22jHUqhtPQWmfevPQRCyfFbdDrKvrfw9oZv5PjaCerQMa3BKkPyUf9yN1CDR3w6";

  private FeeOpReturnImpl feeOpReturnImpl;
  private BIP47Account bip47Account;

  @BeforeEach
  public void setup() throws Exception {
    bip47Account = computeBip47Account(SEED_WORDS_V0, SEED_PASSPHRASE_V0, PCODE_V0);
    feeOpReturnImpl = new FeeOpReturnImplV0(xorMask);
  }

  @Test
  public void computeFeePayload() throws Exception {
    // feeVersion:short(2) | indice:int(4) | feePayload:short(2) | feePartner:short(2)

    // without feePayload
    String feePayload =
        "00000000 00000001 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
    doComputeFeePayload(0, (short) 0, (short) 0, feePayload);

    feePayload =
        "00000000 00000001 00000000 00000000 00000000 00000001 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
    doComputeFeePayload(1, (short) 0, (short) 0, feePayload);

    feePayload =
        "00000000 00000001 00000000 00000001 11100010 01000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
    doComputeFeePayload(123456, (short) 0, (short) 0, feePayload);

    // with feePayload
    feePayload =
        "00000000 00000001 00000000 00000000 00000000 00000000 00000000 00000001 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
    doComputeFeePayload(0, (short) 1, (short) 0, feePayload);

    feePayload =
        "00000000 00000001 00000000 00000000 00000000 00000001 00000000 00000001 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
    doComputeFeePayload(1, (short) 1, (short) 0, feePayload);

    feePayload =
        "00000000 00000001 00000000 00000001 11100010 01000000 00000001 00000001 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
    doComputeFeePayload(123456, (short) 257, (short) 0, feePayload);

    // with feePartner
    feePayload =
        "00000000 00000001 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000001 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
    doComputeFeePayload(0, (short) 0, (short) 1, feePayload);

    feePayload =
        "00000000 00000001 00000000 00000000 00000000 00000001 00000000 00000001 00000011 00111010 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
    doComputeFeePayload(1, (short) 1, (short) 826, feePayload);

    feePayload =
        "00000000 00000001 00000000 00000001 11100010 01000000 00000001 00000001 00110000 00111001 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
    doComputeFeePayload(123456, (short) 257, (short) 12345, feePayload);
  }

  private void doComputeFeePayload(
      int feeIndice, short scodePayload, short partnerPayload, String feePayloadStr)
      throws Exception {
    Assertions.assertEquals(
        feePayloadStr,
        bytesToBinaryString(
            feeOpReturnImpl.computeFeePayload(feeIndice, scodePayload, partnerPayload)));

    byte[] feePayloadBytes = bytesFromBinaryString(feePayloadStr);
    Assertions.assertEquals(feePayloadBytes.length, feeOpReturnImpl.feePayloadLength);

    FeePayloadV1 feePayload = FeePayloadV1.parse(feePayloadBytes);
    Assertions.assertEquals(feeIndice, feePayload.getFeeIndice());
    Assertions.assertEquals(scodePayload, feePayload.getScodePayload());
    Assertions.assertEquals(partnerPayload, feePayload.getFeePartner());
  }

  @Test
  public void computeOpReturn() throws Exception {
    String feePayload =
        "00000000 00000001 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
    String expectedOpReturn =
        "10011101 11000100 01001110 10110010 00101001 00100001 01011111 10101111 01110001 10000000 10101001 10001010 10011101 10000011 10100011 10100100 11101001 11011110 00101010 01001001 00111101 11110000 00110111 01100010 10001101 01100010 11110010 10010001 10110010 00111100 11101111 10001100 01100100 10110011 00010000 11001011 01110101 01011000 11010110 11101001 10011111 10100110 01101100 00010100 10011111 01010101 10010000 10100100 00111101 00100001 10011010 01011100 11011111 00000010 10011000 00010011 10000100 10000111 11101010 01110100 00101111 10011110 01101001 11101001";
    doComputeOpReturn(feePayload, expectedOpReturn);

    feePayload =
        "00000000 00000001 00000000 00000001 11100010 01000000 00000001 00000001 00110000 00111001 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000";
    expectedOpReturn =
        "10011101 11000100 01001110 10110011 11001011 01100001 01011110 10101110 01000001 10111001 10101001 10001010 10011101 10000011 10100011 10100100 11101001 11011110 00101010 01001001 00111101 11110000 00110111 01100010 10001101 01100010 11110010 10010001 10110010 00111100 11101111 10001100 01100100 10110011 00010000 11001011 01110101 01011000 11010110 11101001 10011111 10100110 01101100 00010100 10011111 01010101 10010000 10100100 00111101 00100001 10011010 01011100 11011111 00000010 10011000 00010011 10000100 10000111 11101010 01110100 00101111 10011110 01101001 11101001";
    doComputeOpReturn(feePayload, expectedOpReturn);
  }

  public void doComputeOpReturn(String feePayloadStr, String expectedOpReturn) throws Exception {
    ECKey pk =
        ECKey.fromPrivate(
            new BigInteger(
                "34069012401142361066035129995856280497224474312925604298733347744482107649210"));
    byte[] feePayloadBytes = bytesFromBinaryString(feePayloadStr);

    // encode
    SegwitAddress segwitAddress = new SegwitAddress(pk, params);
    TransactionOutPoint outPoint = mockTxOutput(segwitAddress).getOutPointFor();
    String feePaymentCode = bip47Account.getPaymentCode();
    byte[] opReturn =
        feeOpReturnImpl.computeOpReturn(
            feePaymentCode, feePayloadBytes, outPoint, pk.getPrivKeyBytes());
    String opReturnStr = bytesToBinaryString(opReturn);
    Assertions.assertEquals(expectedOpReturn, opReturnStr);
    Assertions.assertEquals(opReturn.length, feeOpReturnImpl.opReturnLength);

    // decode
    FeeOpReturn feeOpReturn =
        feeOpReturnImpl.parseOpReturn(opReturn, bip47Account, outPoint, pk.getPubKey());
    Assertions.assertArrayEquals(feePayloadBytes, feeOpReturn.getFeePayload());
    Assertions.assertEquals(feeOpReturnImpl.opReturnVersion, feeOpReturn.getOpReturnVersion());
  }
}
