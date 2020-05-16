package com.paratroops;


import com.paratroops.util.CipherKey;
import com.paratroops.util.CipherUtils;
import com.paratroops.util.impl.CipherUtilsImpl;
import junit.framework.TestCase;

public class CipherUtilsTest extends TestCase {

    public void testGenKeyPair() {
        CipherUtils cipher = new CipherUtilsImpl();
        CipherKey[] key = cipher.genKeyPair();

        assertNotNull(key);
        assertNotNull(key[0]);
        assertNotNull(key[1]);
    }

    public void testEncryptAndDecrypt() {
        CipherUtils cipher = new CipherUtilsImpl();
        CipherKey[] key = cipher.genKeyPair();

        String plainText = "Hello world!";
        byte[] encrypted = cipher.encrypt(plainText.getBytes(), key[0]);
        byte[] decrypted = cipher.decrypt(encrypted, key[1]);
        String result = new String(decrypted);

        assertEquals(plainText, result);
    }

}
