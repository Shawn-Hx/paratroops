package com.paratroops.util;

public interface CipherUtils {
    /**
     * 生成公钥私钥对
     * @return ret[0]公钥，ret[1]私钥
     */
    public CipherKey[] genKeyPair();

    /**
     * 加密
     * @param encryptedText 明文
     * @param key 密钥
     * @return 密文
     */
    public byte[] encrypt(byte[] plainText, CipherKey key);

    /**
     * 解密
     * @param encryptedText 密文
     * @param key 密钥
     * @return 明文
     */
    public byte[] decrypt(byte[] encryptedText, CipherKey key);

    /**
     * 比较两个字节序列是否相等
     */
    public boolean compareBytes(byte[] bytes1, byte[] bytes2);

    /**
     * 生成一个字节序列
     */
    public byte[] genBytes();
}