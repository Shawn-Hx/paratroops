package com.paratroops.util.impl;

import com.paratroops.entity.Soldier;
import com.paratroops.util.TroopUtils;

import java.util.List;

public class TroopUtilsImpl implements TroopUtils {

    @Override
    public void despatchPublicKeys(List<Soldier> soldiers) {

    }

    @Override
    public void despathBoxKeyPairs(List<Soldier> soldiers, int threshold, byte[] boxKey) {

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
    public void sortByRank(List<Soldier> soldiers) {

    }

    @Override
    public Soldier electLeader(List<Soldier> soldiers) {
        return null;
    }

    @Override
    public boolean openBox(List<Soldier> soldiers, byte[] boxKey) {
        return false;
    }
}
