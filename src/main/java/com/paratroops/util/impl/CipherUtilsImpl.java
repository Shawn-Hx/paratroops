package com.paratroops.util.impl;

import com.paratroops.util.CipherKey;
import com.paratroops.util.CipherUtils;

import java.math.BigInteger;
import java.util.Random;

public class CipherUtilsImpl implements CipherUtils {
    /**
     * 大素数bit位数
     */
    private static final int PRIME_BIT_LENGTH = 128;
    private static final long E = 65535;

    /**
     * 加密字符串数字基数
     */
    private static final int ENCRYPT_RADIX = 16;
    /**
     * 加密后字符串数字间分隔符
     */
    private static final String SPLIT = ",";

    @Override
    public CipherKey[] genKeyPair() {
        Random random = new Random(1234);

        BigInteger e = BigInteger.valueOf(E);   // e is be fixed 65535 temporarily
        BigInteger p, q, n, phi;
        do {
            p = BigInteger.probablePrime(PRIME_BIT_LENGTH, random);
            q = BigInteger.probablePrime(PRIME_BIT_LENGTH, random);
            n = p.multiply(q);
            phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        } while (!e.gcd(phi).equals(BigInteger.ONE));

        BigInteger d = e.modInverse(phi);

        CipherKey[] key = new CipherKey[2];
        key[0] = new CipherKey(n, e);   // Public key
        key[1] = new CipherKey(n, d);   // Private key

        return key;
    }

    @Override
    public byte[] encrypt(byte[] plainText, CipherKey key) {
        BigInteger n = key.getKey()[0];
        BigInteger e = key.getKey()[1];
        StringBuilder sb = new StringBuilder();

        for (byte b : plainText) {
            int value = Byte.toUnsignedInt(b);
            BigInteger tmp = BigInteger.valueOf(value).modPow(e, n);
            sb.append(tmp.toString(ENCRYPT_RADIX)).append(",");
        }
        return sb.toString().getBytes();
    }

    @Override
    public byte[] decrypt(byte[] encryptedText, CipherKey key) {
        BigInteger n = key.getKey()[0];
        BigInteger d = key.getKey()[1];
        String[] encryptedNums = new String(encryptedText).split(",");
        byte[] res = new byte[encryptedNums.length];

        for (int i = 0; i < encryptedNums.length; i++) {
            BigInteger num = new BigInteger(encryptedNums[i], ENCRYPT_RADIX);
            int value = num.modPow(d, n).intValue();
            res[i] = (byte)value;
        }
        return res;
    }

    @Override
    public boolean compareBytes(byte[] bytes1, byte[] bytes2) {
        return false;
    }

    @Override
    public byte[] genBytes() {
        return new byte[0];
    }

}
