package com.paratroops.util.impl;

import Jama.Matrix;
import com.paratroops.entity.Soldier;
import com.paratroops.gui.JSoldier;
import com.paratroops.util.CipherUtils;
import com.paratroops.util.TroopUtils;

import java.math.BigInteger;
import java.util.*;

public class TroopUtilsImpl implements TroopUtils {

    /**
     * 开箱明文数值的取值上界
     */
    public static final int BOX_KEY_UPPER_BOUNDS = 2048;

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

    private TroopUtilsImpl() {
    }

    @Override
    public void despatchPublicKeys(List<? extends Soldier> soldiers) {
        for (Soldier soldier : soldiers) {
            for (Soldier teammate : soldiers) {
                soldier.addTeamMate(teammate);
            }
        }
    }

    @Override
    public void despathBoxKeyPairs(List<? extends Soldier> soldiers, int threshold, int boxKey) {
        int n = soldiers.size();
        assert threshold <= n;

        Random random = new Random();
        // coefficients 依次存放多项式x的0次项、1次项、...、t-1次项的系数
        int[] coefficients = new int[threshold];
        // 常数项为箱子secret的值
        coefficients[0] = boxKey;
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
    /**
     * 军衔比较
     *
     * @param requester 军衔比较请求发起方
     * @param responsor 军衔比较回复方
     * @return 发起方军衔是否高于回复方
     */
    public boolean compareRank(Soldier requester, Soldier responser) {
        CipherUtilsImpl cipherutil = CipherUtilsImpl.getInstance();
        int[] result = new int[10];
        int compare;
        int prime;
        //若余数数组不符合要求，则重做
        do {
            //生成随机数
            Random rand = new Random();
            int randnum = rand.nextInt(1000) + 2500;
            byte[] message = intToByteArray(randnum);

            //发起方用回复方的公钥对随机数加密后，减去发起方的军衔
            byte[] randnumInt = cipherutil.encrypt(message, responser.getPublicKey());
            byte[] randnumsub = byteSubtract(randnumInt, requester.getRank());
            byte[] randnumAdd;

            //回复方生成大素数prime
            prime = BigInteger.probablePrime(10, new Random()).intValue();
            compare = randnum % prime;

            //回复方对randnumsub分别加0-9后，用回复方的私钥解密，再对prime取模
            for (int i = 0; i < 10; i++) {
                if (i > 0)
                    randnumAdd = byteAdd(randnumsub);
                else
                    randnumAdd = randnumsub;
                byte[] tmp = cipherutil.decrypt(randnumAdd, responser.getPrivateKey());
                result[i] = byteMod(tmp, prime);
            }
        } while (!isLegal(result));

        //回复方将余数数组中大于等于自己军衔索引的元素加1，再发给发起方
        for (int j = responser.getRank(); j < 10; j++) {
            result[j]++;
        }

        //若发起方的军衔索引元素与randnum%prime相等，是则发起方军衔不大于回复方，否则大于
        if (result[requester.getRank()] == compare)
            return false;
        else
            return true;

    }

    /**
     * 将int转化为byte[2]
     *
     * @param i 随机数
     * @return
     */
    private byte[] intToByteArray(int i) {
        byte[] result = new byte[2];
        result[0] = (byte) (i >> 7);
        result[1] = (byte) (i % 128);
        return result;
    }

    /**
     * byte[]减去int（int为10以内的数，byte[]存储16进制的ASCII码）
     *
     * @param subtrahend
     * @param minuend
     * @return byte[]
     */
    private byte[] byteSubtract(byte[] subtrahend, int minuend) {
        int size = subtrahend.length;
        byte[] res = new byte[size];
        for (int i = 0; i < size; i++)
            res[i] = subtrahend[i];
        if (res[size - 2] >= 'a') {
            if (res[size - 2] - 'a' >= minuend)
                res[size - 2] -= minuend;
            else
                res[size - 2] += 10 + '0' - 'a' - minuend;
            return res;
        }
        else if (res[size - 2] - '0' >= minuend) {
            res[size - 2] -= minuend;
            return res;
        }
        int delta = res[size - 2] - '0' + 16 - minuend;
        if (delta >= 10)
            res[size - 2] = (byte) (delta - 10 + 'a');
        else
            res[size - 2] = (byte) (delta + '0');
        int borrow = 1;
        for (int j = size - 3; j >= 0; j--) {
            if (borrow == 0)
                break;
            if (subtrahend[j] == ',')
                continue;
            if (res[j] == 'a') {
                res[j] = '9';
                borrow = 0;
            }
            else if (res[j] != '0') {
                res[j] -= 1;
                borrow = 0;
            }
            else {
                res[j] = 'f';
            }
        }
        return res;
    }

    /**
     * byte[]自增1（byte[]存储16进制的ASCII码）
     *
     * @param addend
     * @return byte[]
     */
    private byte[] byteAdd(byte[] addend) {
        int size = addend.length;

        if (addend[size - 2] < '9' || addend[size - 2] >= 'a' && addend[size - 2] < 'f') {
            addend[size - 2]++;
            return addend;
        }
        else if (addend[size - 2] == '9') {
            addend[size - 2] = 'a';
            return addend;
        }
        addend[size - 2] = '0';
        int carry = 1;
        for (int i = size - 3; i >= 0; i--) {
            if (carry == 0)
                break;
            if (addend[i] == ',')
                continue;
            if (addend[i] == '9') {
                addend[i] = 'a';
                carry = 0;
            }
            else if (addend[i] != 'f') {
                addend[i]++;
                carry = 0;
            }
            else
                addend[i] = '0';
        }
        return addend;
    }

    /**
     * byte[]取模（byte[]存储16进制的ASCII码）
     *
     * @param dividend
     * @param dividor
     * @return int
     */
    private int byteMod(byte[] dividend, int dividor) {
        int remainder = 0;
        int size = dividend.length;
        for (int i = 0; i < size; i++) {
            remainder = (remainder * 128 + dividend[i]) % dividor;
        }
        return remainder;
    }

    /**
     * 判断余数数组是否符合要求：任意两个数之差不小于2
     *
     * @param s
     * @return boolean
     */
    private boolean isLegal(int[] s) {
        int[] cop = new int[s.length];
        for (int i = 0; i < s.length; i++)
            cop[i] = s[i];
        Arrays.sort(cop);
        for (int i = 1; i < cop.length; i++)
            if (cop[i] - cop[i - 1] < 2)
                return false;
        return true;
    }

    @Override
    /**
     * 将士兵按军衔从高到低排序
     */
    public void sortByRank(List<? extends Soldier> soldiers) {
        soldiers.sort((s1, s2) -> {
            if (compareRank(s1, s2))
                return -1;
            else
                return 1;
        });
    }

    //统计每个 voter 的投票情况
    private int[] voting(List<Soldier> candidate, List<Soldier> voter, List<List<Soldier>> vote_map) {
        int candidate_num = candidate.size();
        int voter_num = voter.size();
        // 每个候选人的最多得票数
        int k = (int) (Math.floor(Math.log(voter_num)/Math.log(2)) + 1);
        // 选举总位数
        int bits = candidate_num*k;

        // 由于当前记录编码使用 int 型，因此不支持数量特别多的 candidate 或 voter
        int[] arr_vote = new int[voter_num];
        // encode 每个 voter 的投票情况
        char[] str_bits = new char[bits];
        int i = 0;
        String str;
        for (i = 0;i < voter_num;i++){
            Arrays.fill(str_bits, '0');
            for(int j = 0;j < candidate_num;j++){
                if (vote_map.get(i).contains(candidate.get(j))){
                    // 为对应的候选者投一票
                    str_bits[k*(j+1)-1] = '1';
                }
            }
            str = new String(str_bits);
            arr_vote[i] = Integer.parseInt(str, 2);
        }
        return arr_vote;
    }

    // difussion过程，混淆每个 voter 的投票情况
    private int[][] casting(int []arr_vote) {
        int voter_num = arr_vote.length;
        int[][] transfer_matrix = new int[voter_num][voter_num];
        Random random = new Random();

        for(int i = 0;i < voter_num;i++) {
            int tmp = arr_vote[i];
            // 将第 i 个人的投票情况分散开
            for(int j = 0;j < voter_num - 1;j++) {
                transfer_matrix[i][j] = random.nextInt(tmp+1);
                tmp = tmp - transfer_matrix[i][j];
            }
            transfer_matrix[i][voter_num - 1] = tmp;
        }
        // 每个 voter 向其他人广播自己的投票结果
        return transfer_matrix;
    }

    // 随机验证 voter 的投票是否合法，统计得票数最高的候选者
    private int[][] broadcast_vote(int[][] transfer_matrix, List<Soldier> candidate, List<Soldier> voter) {
        int voter_num = voter.size();
        int[] vote_sum = new int[voter_num];
        int[][] broadcast = new int[voter_num][voter_num];

        // 每个 voter 求和自己收到的投票情况
        for (int i = 0; i < voter_num; i++) {
            for (int j = 0; j < voter_num; j++) {
                vote_sum[i] += transfer_matrix[j][i];
            }
        }
        //每个 voter 向所有人广播自身求和结果
        for (int i = 0; i < voter_num; i++) {
            for (int j = 0; j < voter_num; j++) {
                broadcast[i][j] = vote_sum[i];
            }
        }
        return broadcast;
    }

    private int verify(int[][] broadcast, int index, int candidate_num, int voter_num){
        int result = 0;
        int[] arr_votes = new int[candidate_num];
        for(int i = 0;i < voter_num;i++){
            // 统计收到的全部信息
            result += broadcast[i][index];
        }

        int k = (int) (Math.floor(Math.log(voter_num)/Math.log(2)) + 1); //每个候选人对应的二进制位数
        String str = Integer.toBinaryString(result);

        // 解析每个候选者的得票情况
        int length = str.length();
        for(int i = (candidate_num - 1);i >= 0;i--){
            if(length >= k){
                String tmp = str.substring(length - k, length);
                arr_votes[i] = Integer.parseInt(tmp,2);
            }else if(length > 0){
                String tmp = str.substring(0, length);
                arr_votes[i] = Integer.parseInt(tmp,2);
            }else{
                arr_votes[i] = 0;
            }
            length -= k;
        }

        int max_index = 0;
        for(int i = 0;i < candidate_num;i++) {
            if (arr_votes[i] > arr_votes[max_index]) {
                max_index = i;
            }
        }
//        System.out.println("投票结果：" + max_index);
        return max_index;
    }

    private int electronicVoting(List<Soldier> candidate, List<Soldier> voter) {
        Random rand = new Random();
        List<List<Soldier>> vote_map = new ArrayList<>();
        for (int i = 0;i < voter.size();i++){
            List<Soldier> list = new ArrayList<>();
            for (Soldier soldier : candidate) {
                // 假设每位候选人有 1/3 的几率被选中
                if (rand.nextInt(3) == 0) {
                    list.add(soldier);
                }
            }
            vote_map.add(list);
        }
        // 统计每个候选人的投票数
        int[] arr_vote = voting(candidate, voter, vote_map);
        int[][] transfer_matrix = casting(arr_vote);
        int[][] broadcast = broadcast_vote(transfer_matrix, candidate, voter);
        Random random = new Random();
        int index = random.nextInt(voter.size());
        // 输出任一士兵的验证结果
        return verify(broadcast, index, candidate.size(), voter.size());
    }

    @Override
    public Soldier selectLeader(List<? extends Soldier> soldiers) {
        sortByRank(soldiers);
        if (soldiers.size() < 2 || !compareRank(soldiers.get(1), soldiers.get(0))){
            return soldiers.get(0);
        }
        // 获取 rank 最高的所有士兵
        List<Soldier> candidate = new ArrayList<>();
        List<Soldier> voter = new ArrayList<>();
        candidate.add(soldiers.get(0));
        for (int i = 1;i < soldiers.size();i++){
            if (compareRank(soldiers.get(i), soldiers.get(0))){
                candidate.add(soldiers.get(i));
            }else{
                voter.add(soldiers.get(i));
            }
        }
        System.out.println("有" + candidate.size() + "个士兵具有最高军衔");
        System.out.println("有" + voter.size() + "个士兵进行投票");
        // 没有投票的士兵，进行随机选取
        if (voter.size() == 0){
            Random random = new Random();
            return candidate.get(random.nextInt(candidate.size()));
        }
        // 进行电子投票
        int id = electronicVoting(candidate, voter);

        return candidate.get(id);
    }

    @Override
    public boolean openBox(List<? extends Soldier> soldiers, int boxKey) {
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
                coefficient[i][j] = coefficient[i][j - 1] * keyPair[0];
            }
        }
        // 求解线性方程组
        Matrix A = new Matrix(coefficient);
        Matrix b = new Matrix(bb);
        Matrix x = A.solve(b);
        // 获取构造的多项式的常数项值
        double f0 = x.getArray()[0][0];

        // 四舍五入取整后比较
        return Math.round(f0) == boxKey;
    }

}
