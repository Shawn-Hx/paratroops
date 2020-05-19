package com.paratroops.util.impl;

import com.paratroops.entity.Soldier;
import com.paratroops.util.CipherUtils;
import com.paratroops.util.TroopUtils;

import java.util.List;

public class TroopUtilsImpl implements TroopUtils {

    /**
     * 持有CipherUtils引用
     */
    private CipherUtils cipherUtils = CipherUtilsImpl.getInstance();

    /**
     * 单例
     */
    private static TroopUtilsImpl instance;

    public static TroopUtils getInstance() {
        if (instance == null) {
            instance = new TroopUtilsImpl();
        }
        return instance;
    }

    @Override
    public void despatchPublicKeys(List<? extends Soldier> soldiers) {
        for (Soldier soldier : soldiers) {
            for (Soldier teammate : soldiers) {
                if (soldier == teammate) {
                    soldier.addTeamMate(teammate);
                }
            }
        }
    }

    @Override
    public void despathBoxKeyPairs(List<? extends Soldier> soldiers, int threshold, byte[] boxKey) {

    }

    @Override
    public boolean authenticate(Soldier requester, Soldier checker) {
        return false;
    }

    @Override
    public boolean compareRank(Soldier requester, Soldier responser) {
        return false;
    }

    @Override
    public void sortByRank(List<? extends Soldier> soldiers) {

    }

    @Override
    public Soldier electLeader(List<? extends Soldier> soldiers) {
        return null;
    }

    @Override
    public boolean openBox(List<? extends Soldier> soldiers, byte[] boxKey) {
        return false;
    }
}
