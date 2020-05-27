package com.paratroops.util;

import java.math.BigInteger;

public class CipherKey {

    /**
     * key[0]:  N (=p*q)
     * key[1]:  d if private key
     * e if public key
     */
    private BigInteger[] key;

    public CipherKey(BigInteger n, BigInteger other) {
        this.key = new BigInteger[]{n, other};
    }

    public BigInteger[] getKey() {
        return key;
    }

    public void setKey(BigInteger[] key) {
        this.key = key;
    }

    public String keyToString() {
        return "(" + key[0].toString(16) + "," + key[1].toString(16) + ")";
    }
}