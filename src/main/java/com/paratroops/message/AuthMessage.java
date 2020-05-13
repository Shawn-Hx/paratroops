package com.paratroops.message;

/**
 * 士兵之间认证通讯的消息对象封装
 */
public class AuthMessage {
    /**
     * 明文
     */
    private byte[] plainText;
    
    /**
     * 密文
     */
    private byte[] encryptedText;

    public AuthMessage(byte[] plainText, byte[] encryptedText) {
        this.plainText = plainText;
        this.encryptedText = encryptedText;
    }

    public byte[] getPlainText() {
        return plainText;
    }

    public void setPlainText(byte[] plainText) {
        this.plainText = plainText;
    }

    public byte[] getEncryptedText() {
        return encryptedText;
    }

    public void setEncryptedText(byte[] encryptedText) {
        this.encryptedText = encryptedText;
    }
}