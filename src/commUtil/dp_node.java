package commUtil;

import java.util.ArrayList;

public class dp_node {
	/**
	 * 
	 * DP 函数是0-1背包问题的解决方案
	 * 
	 * @author fb
	 * @param V:各个物品的体积(size
	 *            为n+1)
	 * @param P:各个物品的价值(size
	 *            为n+1)
	 * @param c：背包的容量
	 * @param n：物品的个数
	 * @param mincap:最小的虚拟机计算能力
	 * @param 注意：V,P第一个为0，无意义，为了数组索引有物理意义（dp的索引为容量）
	 * @return 背包选择最大价值的具体方案
	 *         采用一维数组的方式节省空间，状态转移方程为：dp[i][j]=max(dp[i-1][j],dp[i-1][j-V[i]]+P[i])
	 */
	public static ArrayList<Integer> KnapsackDP(ArrayList<Integer> V, ArrayList<Integer> P, int c, int n, int req) {
		int[] dp = new int[c + 1];
		ArrayList<ArrayList<Integer>> pre_node = new ArrayList<ArrayList<Integer>>(c + 1);
		for (Integer j = 0; j < c + 1; j++) {
			pre_node.add(j, new ArrayList<Integer>());
		}
		for (int i = 1; i < n + 1; i++)
			for (int j = c; j >= V.get(i); j--) {
				if (dp[j] < dp[j - V.get(i)] + P.get(i)) {
					dp[j] = dp[j - V.get(i)] + P.get(i);
					ArrayList<Integer> arrayList = pre_node.get(j - V.get(i));
					pre_node.set(j, (ArrayList<Integer>) arrayList.clone());
					pre_node.get(j).add(i);
				}
			}
		if (dp[c] >= req) {
			return pre_node.get(c);
		} else {
			return null;
		}
	}
}