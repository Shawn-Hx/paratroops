package com.paratroops;

import com.paratroops.entity.Soldier;
import com.paratroops.util.CipherUtils;
import com.paratroops.util.TroopUtils;
import com.paratroops.util.impl.CipherUtilsImpl;
import com.paratroops.util.impl.TroopUtilsImpl;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class TroopUtilsTest extends TestCase {

    public CipherUtils cipherUtils = CipherUtilsImpl.getInstance();
    public TroopUtils troopUtils = TroopUtilsImpl.getInstance();

    public void testOpenBox() {
        List<Soldier> soldiers = new ArrayList<>();
        soldiers.add(new Soldier(1, cipherUtils));
        soldiers.add(new Soldier(2, cipherUtils));
        soldiers.add(new Soldier(3, cipherUtils));
        soldiers.add(new Soldier(4, cipherUtils));
        soldiers.add(new Soldier(5, cipherUtils));

        int secretBoxKey = 23;
        int threshold = 3;
        troopUtils.despathBoxKeyPairs(soldiers, threshold, secretBoxKey);

        List<Soldier> openBoxSoldiers = new ArrayList<>();

        // 2 4 5 号伞兵开锁，应成功
        openBoxSoldiers.add(soldiers.get(1));
        openBoxSoldiers.add(soldiers.get(3));
        openBoxSoldiers.add(soldiers.get(4));
        assertTrue(troopUtils.openBox(openBoxSoldiers, secretBoxKey));

        // 2 3 4 5 号伞兵开锁，应成功
        openBoxSoldiers.clear();
        openBoxSoldiers.add(soldiers.get(1));
        openBoxSoldiers.add(soldiers.get(2));
        openBoxSoldiers.add(soldiers.get(3));
        openBoxSoldiers.add(soldiers.get(4));
        assertTrue(troopUtils.openBox(openBoxSoldiers, secretBoxKey));

        // 1 2 号伞兵开锁，应失败
        openBoxSoldiers.clear();
        openBoxSoldiers.add(soldiers.get(0));
        openBoxSoldiers.add(soldiers.get(1));
        assertFalse(troopUtils.openBox(openBoxSoldiers, secretBoxKey));
    }

}
