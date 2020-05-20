package com.paratroops.util.impl;

import Jama.Matrix;
import com.paratroops.entity.Soldier;
import com.paratroops.util.CipherUtils;
import com.paratroops.util.TroopUtils;

import java.util.List;
import java.util.Random;

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

    private TroopUtilsImpl() {}

    @Override
    public void despatchPublicKeys(List<? extends Soldier> soldiers) {
        for (Soldier soldier : soldiers) {
            for (Soldier teammate : soldiers) {
                soldier.addTeamMate(teammate);
            }
        }
    }

    // TODO boxKey参数类型
    @Override
    public void despathBoxKeyPairs(List<? extends Soldier> soldiers, int threshold, byte[] boxKey) {
        int n = soldiers.size();
        assert threshold <= n;

        Random random = new Random();
        // coefficients 依次存放多项式x的0次项、1次项、...、t-1次项的系数
        int[] coefficients = new int[threshold];
        // 常数项固定为secret的值
        coefficients[0] = boxKey[0];
        // 生成随机的多项式系数
        for (int i = 1; i < coefficients.length; i++) {
            coefficients[i] = 1 + random.nextInt(256);
        }
        // 分发
        int num = 1;
        for (Soldier soldier : soldiers) {
            long x = num;
            long y = coefficients[threshold - 1];

            for (int i = threshold - 2; i >= 0; i--) {
                y = y * x + coefficients[i];
            }
            soldier.setBoxKeyPair(new long[]{x, y});
            num++;
        }
    }

    @Override
    public boolean authenticate(Soldier requester, Soldier checker) {
        return checker.checkAuthRequest(requester);
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

    // TODO boxKey 参数类型
    @Override
    public boolean openBox(List<? extends Soldier> soldiers, byte[] boxKey) {
        // TODO soldiers是所有伞兵还是仅有开箱子的伞兵？
        int n = soldiers.size();
        double[][] coefficient = new double[n][n];
        double[][] bb = new double[n][1];
        // 构造系数矩阵及右端向量
        for (int i = 0; i < n; i++) {
            Soldier soldier = soldiers.get(i);
            long[] keyPair = soldier.getBoxKeyPair();
            bb[i][0] = keyPair[1];

            coefficient[i][0] = 1;
            for (int j = 1; j < n; j++) {
                coefficient[i][j] =  coefficient[i][j-1] * keyPair[0];
            }
        }
        // 求解线性方程组
        Matrix A = new Matrix(coefficient);
        Matrix b = new Matrix(bb);
        Matrix x = A.solve(b);
        // 获取构造的多项式的常数项值
        double f0 = x.getArray()[0][0];

        // 四舍五入取整后比较
        return Math.round(f0) == boxKey[0];
    }

}
