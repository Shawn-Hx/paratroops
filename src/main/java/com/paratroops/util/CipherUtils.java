package com.paratroops.util;

import java.util.List;

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

    /**
     * 在地图上随机采样不重复的若干个位置
     * @param SIZE 地图尺寸, SIZE[0] * SIZE[1]
     * @param numPos 采样的不重复的位置个数
     * @return 长度为{@code numPos}的序列，序列中每个整数n代表一个位置坐标(n/SIZE[1], n%SIZE[1])
     */
    public List<Integer> samplePositions(int[] SIZE, int numPos);

    /**
     * 生成随机军衔序列
     * @param numRank 随机军衔序列长度
     * @param highestRank 最高可取的军衔
     * @param numHighestRank 序列中最高军衔出现次数
     */
    public List<Integer> genRandomRankList(int numRank, int highestRank, int numHighestRank);
}