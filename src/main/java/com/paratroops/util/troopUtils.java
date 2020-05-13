package com.paratroops.util;

import java.util.List;

import com.paratroops.entity.Soldier;

public interface troopUtils {
    /**
     * 跳伞前给同一队伍的所有士兵分发其所有队友的公钥
     * @param soldiers 同一队伍的士兵
     */
    public void despatchPublicKeys(List<Soldier> soldiers);

    /**
     * 跳伞前给同一队伍的所有士兵分发用于开箱的密钥对
     * @param soldiers 同一队伍的所有士兵
     * @param threshold 开箱门限，至少{@code threshold}个士兵才能开箱
     * @param boxKey 待加密的箱子明文
     */
    public void despathBoxKeyPairs(List<Soldier> soldiers, int threshold, byte[] boxKey);

    /**
     * 身份认证
     * @param requester 身份认证请求发起方
     * @param checker 身份认证校验方
     * @return 校验方是否成功认证发起方身份
     */
    public boolean authenticate(Soldier requester, Soldier checker);
    
    /**
     * 军衔比较
     * @param requester 军衔比较请求发起方
     * @param checker 军衔比较回复方
     * @return 发起方军衔是否高于回复方
     */
    public boolean compareRank(Soldier requester, Soldier responser);

    /**
     * 将士兵按军衔从高到低排序
     */
    public void sortByRank(List<Soldier> soldiers);

    /**
     * 从一队士兵中按照军衔选举指挥官
     */
    public Soldier electLeader(List<Soldier> soldiers);

    /**
     * 判断给定的这些士兵能否开箱
     * @param soldiers 选取的士兵
     * @param boxKey 箱子明文
     * @return 是否开箱成功
     */
    public boolean openBox(List<Soldier> soldiers, byte[] boxKey);
}