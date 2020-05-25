package com.paratroops.entity;

import java.util.HashMap;

import com.paratroops.message.AuthMessage;
import com.paratroops.util.CipherKey;
import com.paratroops.util.CipherUtils;

/**
 * 多方安全计算的主体：士兵对象
 */
public class Soldier {
    /**
     * 最高军衔
     */
    public static final int HIGHEST_RANK = 8;


    /**
     * 军衔
     */
    protected int rank;

    /**
     * 本人的公钥私钥对
     * keyPair[0]: 公钥
     * keyPair[1]: 私钥
     */
    private CipherKey[] keyPair;

    /**
     * 用于开箱的(x, y)队
     * boxKeyPair[0]: x
     * boxKeyPair[1]: y
     */
    private long[] boxKeyPair;

    /**
     * 队友们的公钥表：士兵对象 -> 公钥
     */
    private HashMap<Soldier, CipherKey> teamKeys;

    /**
     * 密码学算法封装
     */
    private CipherUtils cipherUtils;

    public Soldier(int rank, CipherUtils cipherUtils) {
        this.rank = rank;
        this.cipherUtils = cipherUtils;
        this.keyPair = cipherUtils.genKeyPair();
        this.teamKeys = new HashMap<>();
    }

    public CipherKey getPublicKey() {
        return this.keyPair[0];
    }

    public CipherKey getPrivateKey() {
        return this.keyPair[1];
    }

    public long[] getBoxKeyPair() {
        return this.boxKeyPair;
    }

    public int getRank() {
        return this.rank;
    }

    public void setBoxKeyPair(long[] boxKeyPair) {
        this.boxKeyPair = boxKeyPair;
    }

    /**
     * 保存队友的公钥
     * @param other
     */
    public void addTeamMate(Soldier other) {
        this.teamKeys.put(other, other.getPublicKey());
    }

    /**
     * 校验对方发来的认证信息
     * @return 是否认证成功（属于同一个队伍）
     */
    public boolean checkAuthRequest(Soldier other) {
        AuthMessage message = other.sendAuthRequest();
        if (!teamKeys.containsKey(other)) {
            return false;
        }
        CipherKey publicKey = teamKeys.get(other);
        byte[] decrypted = cipherUtils.decrypt(message.getEncryptedText(), publicKey); 
        return cipherUtils.compareBytes(decrypted, message.getPlainText());
    }

    /**
     * 向对方发送校验请求
     * @return 发送的校验信息，包括明文和密文
     */
    public AuthMessage sendAuthRequest() {
        byte[] plainText = cipherUtils.genBytes();
        byte[] encrpytedText = cipherUtils.encrypt(plainText, this.keyPair[1]);
        return new AuthMessage(plainText, encrpytedText);
    }
}