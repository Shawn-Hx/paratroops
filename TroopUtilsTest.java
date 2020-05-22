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


    public void testCompareRank() {
        CipherUtils cipherUtils = CipherUtilsImpl.getInstance();
        Soldier req = new Soldier(4, cipherUtils);
        Soldier res1 = new Soldier(6, cipherUtils);
        Soldier res2 = new Soldier(1, cipherUtils);
        boolean comp1 = troopUtils.compareRank(req, res1);
        boolean comp2 = troopUtils.compareRank(req, res2);
        assertFalse(comp1);
        assertTrue(comp2);
    }

    public void testSortByRank() {
        CipherUtils cipherUtils = CipherUtilsImpl.getInstance();
        Soldier s1 = new Soldier(4, cipherUtils);
        Soldier s2 = new Soldier(6, cipherUtils);
        Soldier s3 = new Soldier(1, cipherUtils);
        Soldier s4 = new Soldier(2, cipherUtils);
        Soldier s5 = new Soldier(9, cipherUtils);

        List<Soldier> sold = new ArrayList<Soldier>();
        sold.add(s1);
        sold.add(s2);
        sold.add(s3);
        sold.add(s4);
        sold.add(s5);

        //对5个不同军衔的士兵排序
        troopUtils.sortByRank(sold);
        assertEquals(sold.get(0), s5);
        assertEquals(sold.get(1), s2);
        assertEquals(sold.get(2), s1);
        assertEquals(sold.get(3), s4);
        assertEquals(sold.get(4), s3);

        Soldier s6 = new Soldier(4, cipherUtils);
        Soldier s7 = new Soldier(9, cipherUtils);

        sold.add(s6);
        sold.add(s7);

        //s1和s6的军衔、s5和s7军衔分别相同情况的排序
        troopUtils.sortByRank(sold);
        assertEquals(sold.get(0), s7);
        assertEquals(sold.get(1), s5);
        assertEquals(sold.get(2), s2);
        assertEquals(sold.get(3), s6);
        assertEquals(sold.get(4), s1);
        assertEquals(sold.get(5), s4);
        assertEquals(sold.get(6), s3);
    }

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
