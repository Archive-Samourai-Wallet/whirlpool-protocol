package com.samourai.whirlpool.protocol.util;

import com.samourai.wallet.bip47.rpc.BIP47Account;
import com.samourai.wallet.segwit.SegwitAddress;
import com.samourai.whirlpool.protocol.AbstractTest;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.TransactionOutPoint;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class XorMaskTest extends AbstractTest {
  private BIP47Account bip47Account;
  private ECKey inputKey;

  @BeforeEach
  public void setup() throws Exception {
    bip47Account = computeBip47Account();
    inputKey =
        ECKey.fromPrivate(
            new BigInteger(
                "51732494501375812428458532778173039831982719040289801757669840732818870607903"));
  }

  @Test
  public void mask() throws Exception {
    byte[] dataToMask = ByteBuffer.allocate(64).putInt(123456).array();
    Assertions.assertEquals(
        "0001e240000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
        Hex.toHexString(dataToMask));

    String inputOutPointAddress = new SegwitAddress(inputKey, params).getBech32AsString();
    TransactionOutPoint inputOutPoint =
        cryptoTestUtil.generateTransactionOutPoint(inputOutPointAddress, 1234, params);

    // mask
    byte[] dataMasked =
        xorMask.mask(
            dataToMask, bip47Account.getPaymentCode(), inputKey.getPrivKeyBytes(), inputOutPoint);
    Assertions.assertEquals(
        "10d8bb0615984f519c7e1a82fe44ee8993bf58fffe42ad8515f7e8cb691d4df9375b88c1de26ccef8dfce7f91b768603f8f93108f8e3b35db56018207a5918db",
        Hex.toHexString(dataMasked));
  }

  @Test
  public void unmask() throws Exception {
    byte[] dataMasked =
        Hex.decode(
            "10d8bb0615984f519c7e1a82fe44ee8993bf58fffe42ad8515f7e8cb691d4df9375b88c1de26ccef8dfce7f91b768603f8f93108f8e3b35db56018207a5918db");

    String inputOutPointAddress = new SegwitAddress(inputKey, params).getBech32AsString();
    TransactionOutPoint inputOutPoint =
        cryptoTestUtil.generateTransactionOutPoint(inputOutPointAddress, 1234, params);

    // unmask
    byte[] dataUnmasked =
        xorMask.unmask(dataMasked, bip47Account, inputOutPoint, inputKey.getPubKey());

    // verify
    Assertions.assertEquals(
        "0001e240000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
        Hex.toHexString(dataUnmasked));
  }

  @Test
  public void maskUnmask() throws Exception {
    byte[] dataToMask = ByteBuffer.allocate(64).putInt(123456).array();

    String inputOutPointAddress = new SegwitAddress(inputKey, params).getBech32AsString();
    TransactionOutPoint inputOutPoint =
        cryptoTestUtil.generateTransactionOutPoint(inputOutPointAddress, 1234, params);

    // mask
    byte[] dataMasked =
        xorMask.mask(
            dataToMask, bip47Account.getPaymentCode(), inputKey.getPrivKeyBytes(), inputOutPoint);
    Assertions.assertEquals(
        "10d8bb0615984f519c7e1a82fe44ee8993bf58fffe42ad8515f7e8cb691d4df9375b88c1de26ccef8dfce7f91b768603f8f93108f8e3b35db56018207a5918db",
        Hex.toHexString(dataMasked));
    Assertions.assertEquals(
        "00010000 11011000 10111011 00000110 00010101 10011000 01001111 01010001 10011100 01111110 00011010 10000010 11111110 01000100 11101110 10001001 10010011 10111111 01011000 11111111 11111110 01000010 10101101 10000101 00010101 11110111 11101000 11001011 01101001 00011101 01001101 11111001 00110111 01011011 10001000 11000001 11011110 00100110 11001100 11101111 10001101 11111100 11100111 11111001 00011011 01110110 10000110 00000011 11111000 11111001 00110001 00001000 11111000 11100011 10110011 01011101 10110101 01100000 00011000 00100000 01111010 01011001 00011000 11011011",
        bytesToBinaryString(dataMasked));

    // unmask
    byte[] dataUnmasked =
        xorMask.unmask(dataMasked, bip47Account, inputOutPoint, inputKey.getPubKey());

    // verify
    Assertions.assertArrayEquals(dataToMask, dataUnmasked);
  }

  @Test
  public void toMaskLengthFromMaskLength() throws Exception {
    int PAYLOAD_LENGTH = 50;
    byte[] dataToMask = ByteBuffer.allocate(PAYLOAD_LENGTH).putInt(123456).array();
    Assertions.assertEquals(PAYLOAD_LENGTH, dataToMask.length);
    Assertions.assertEquals(
        "00000000 00000001 11100010 01000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
        bytesToBinaryString(dataToMask));

    // toMaskLength
    byte[] dataToMaskLength = xorMask.toMaskLength(dataToMask);
    Assertions.assertEquals(XorMask.MASK_LENGTH, dataToMaskLength.length);
    Assertions.assertEquals(
        "00000000 00000001 11100010 01000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000",
        bytesToBinaryString(dataToMaskLength));

    // fromMaskLength
    byte[] dataToMask2 = xorMask.fromMaskLength(dataToMaskLength, PAYLOAD_LENGTH);
    Assertions.assertEquals(PAYLOAD_LENGTH, dataToMask2.length);
    Assertions.assertArrayEquals(dataToMask2, dataToMask);
  }

  @Test
  public void maskUnmaskLength() throws Exception {
    int PAYLOAD_LENGTH = 50;
    byte[] dataToMask = ByteBuffer.allocate(PAYLOAD_LENGTH).putInt(123456).array();

    String inputOutPointAddress = new SegwitAddress(inputKey, params).getBech32AsString();
    TransactionOutPoint inputOutPoint =
        cryptoTestUtil.generateTransactionOutPoint(inputOutPointAddress, 1234, params);

    // mask
    byte[] dataMasked =
        xorMask.maskToLength(
            dataToMask, bip47Account.getPaymentCode(), inputKey.getPrivKeyBytes(), inputOutPoint);
    Assertions.assertEquals(PAYLOAD_LENGTH, dataMasked.length);
    Assertions.assertEquals(
        "10d8bb0615984f519c7e1a82fe44ee8993bf58fffe42ad8515f7e8cb691d4df9375b88c1de26ccef8dfce7f91b768603f8f9",
        Hex.toHexString(dataMasked));
    Assertions.assertEquals(
        "00010000 11011000 10111011 00000110 00010101 10011000 01001111 01010001 10011100 01111110 00011010 10000010 11111110 01000100 11101110 10001001 10010011 10111111 01011000 11111111 11111110 01000010 10101101 10000101 00010101 11110111 11101000 11001011 01101001 00011101 01001101 11111001 00110111 01011011 10001000 11000001 11011110 00100110 11001100 11101111 10001101 11111100 11100111 11111001 00011011 01110110 10000110 00000011 11111000 11111001",
        bytesToBinaryString(dataMasked));

    // unmask
    byte[] dataUnmasked =
        xorMask.unmaskToLength(
            dataMasked, bip47Account, inputOutPoint, inputKey.getPubKey(), PAYLOAD_LENGTH);

    // verify
    Assertions.assertArrayEquals(dataToMask, dataUnmasked);
  }
}
