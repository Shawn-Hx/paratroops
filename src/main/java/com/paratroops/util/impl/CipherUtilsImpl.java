package com.paratroops.util.impl;

import com.paratroops.util.CipherKey;
import com.paratroops.util.CipherUtils;

import java.math.BigInteger;
import java.util.Random;

public class CipherUtilsImpl implements CipherUtils {

    /**
     * 单例
     */
    private static CipherUtilsImpl instance;

    public static CipherUtilsImpl getInstance() {
        if (instance == null) {
            instance = new CipherUtilsImpl();
        }
        return instance;
    }

    private CipherUtilsImpl() {}

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
        Random random = new Random();

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
            sb.append(tmp.toString(ENCRYPT_RADIX)).append(SPLIT);
        }
        return sb.toString().getBytes();
    }

    @Override
    public byte[] decrypt(byte[] encryptedText, CipherKey key) {
        BigInteger n = key.getKey()[0];
        BigInteger d = key.getKey()[1];
        String[] encryptedNums = new String(encryptedText).split(SPLIT);
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
        if (bytes1.length != bytes2.length)
            return false;
        for (int i = 0; i < bytes1.length; i++) {
            if (bytes1[i] != bytes2[i])
                return false;
        }
        return true;
    }

    private static final int MIN_LENGTH = 64;       // 随机字节数组最短长度（包含该长度）
    private static final int MAX_LENGTH = 256;      // 随机字节数组最长长度（不包含该长度）

    @Override
    public byte[] genBytes() {
        Random random = new Random();
        int length = MIN_LENGTH + random.nextInt(MAX_LENGTH - MIN_LENGTH);
        byte[] res = new byte[length];
        random.nextBytes(res);
        return res;
    }

}
