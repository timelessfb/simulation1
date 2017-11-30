package Driver;

import algorithm.CompareMapping;
import algorithm.Mapping;
import algorithm.RandomMapping;
import commUtil.SliceGenerate;

import java.util.ArrayList;

/**
 * @see 归一化的目标函数值（资源、节点、总资源）
 * @author fb
 *
 */
public class algsimv2_1 {

	static Integer K = 0;
	static Integer Kmax = 5;
	static Integer I = 10;
	static Integer H = 100;// H = 1000;
	static ArrayList<Integer> C = new ArrayList<Integer>(I * H + 1);
	static int[][] B = new int[I][I];
	static ArrayList<Integer> node_req = new ArrayList<Integer>(Kmax);
	static ArrayList<Integer> link_req = new ArrayList<Integer>(Kmax - 1);
	static Integer R = 10;// 多次取平均的次数
	static Integer delT = 20;// 点数delT=10
	static Integer delScling = 10;// 每个时间间隔的切片请求数
	static int nodeBound = 200;
	static int linkBound = 50;

	public static void main(String[] arg) {
		int[] cap = { 4, 9, 16, 25, 36, 49, 81, 100, 150, 300 };
		C.add(Integer.MAX_VALUE);
		for (int i = 0; i < I; i++) {
			for (int j = 0; j < H; j++) {
				C.add(cap[9 - i]);
			}
		}

		for (int i = 0; i < I; i++) {
			for (int j = i; j < I; j++) {
				B[i][j] = linkBound * 2;
				B[j][i] = B[i][j];
			}
		}

		for (int i = 0; i < I; i++) {
			for (int j = 0; j < I; j++) {
				if ((i % 2) == (j % 2)) {
					B[i][j] = 1000000;
				}
			}
		}

		for (int i = 0; i < I; i++) {
			for (int j = 0; j < I; j++) {
				if ((i % 5) == (j % 5)) {
					B[i][j] = 1000000;
				}
			}
		}
		for (int i = 0; i < I; i++) {
			for (int j = 0; j < I; j++) {
				if ((i % 7) == (j % 7)) {
					B[i][j] = 1000000;
				}
			}
		}
		for (int i = 0; i < I; i++) {
			System.out.println();
			for (int j = 0; j < I; j++) {
				System.out.print(B[i][j] + ",");
			}
		}
		ArrayList<ArrayList<ArrayList<Integer>>> resultSet = new ArrayList<ArrayList<ArrayList<Integer>>>();
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		for (int r = 0; r < R; r++) {
			result = test();
			resultSet.add((ArrayList<ArrayList<Integer>>) result.clone());
		}

		ArrayList<ArrayList<Integer>> avgResult = new ArrayList<ArrayList<Integer>>();
		avgResult.add(new ArrayList<Integer>());
		avgResult.add(new ArrayList<Integer>());
		avgResult.add(new ArrayList<Integer>());
		for (int t = 0; t < delT; t++) {
			Integer r0 = 0, r1 = 0, r2 = 0;
			for (int r = 0; r < R; r++) {
				r0 = resultSet.get(r).get(0).get(t);
				r1 = resultSet.get(r).get(1).get(t);
				r2 = resultSet.get(r).get(2).get(t);
			}
			avgResult.get(0).add(r0);
			avgResult.get(1).add(r1);
			avgResult.get(2).add(r2);
		}
		System.out.println();
		System.out.println(avgResult);
	}

	public static ArrayList<ArrayList<Integer>> test() {
		ArrayList<Integer> C1 = new ArrayList<Integer>(I * H + 1);
		C1 = ((ArrayList<Integer>) C.clone());
		ArrayList<Integer> C2 = new ArrayList<Integer>(I * H + 1);
		C2 = ((ArrayList<Integer>) C.clone());
		ArrayList<Integer> C3 = new ArrayList<Integer>(I * H + 1);
		C3 = ((ArrayList<Integer>) C.clone());
		int[][] B1 = new int[I][I];
		int[][] B2 = new int[I][I];
		int[][] B3 = new int[I][I];

		for (int i = 0; i < I; i++) {
			for (int j = 0; j < I; j++) {
				B1[i][j] = B[i][j];
			}
		}
		for (int i = 0; i < I; i++) {
			for (int j = 0; j < I; j++) {
				B2[i][j] = B[i][j];
			}
		}

		for (int i = 0; i < I; i++) {
			for (int j = 0; j < I; j++) {
				B3[i][j] = B[i][j];
			}
		}

		/**
		 * 初始化node_req和link_req
		 */
		for (int i = 0; i < Kmax; i++) {
			node_req.add(0);
		}
		for (int i = 0; i < Kmax - 1; i++) {
			link_req.add(0);
		}

		/**
		 * 迭代映射切片请求
		 */
		// result.get(i):第i种算法；result.get(i).get(j):第i种算法的第j个时刻
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		result.add(new ArrayList<Integer>());
		result.add(new ArrayList<Integer>());
		result.add(new ArrayList<Integer>());
		Integer totalResourceUsed1 = 0, totalResourceUsed2 = 0, totalResourceUsed3 = 0;
		Integer nodeResourceUsed1 = 0, nodeResourceUsed2 = 0, nodeResourceUsed3 = 0;
		Integer linkResourceUsed1 = 0, linkResourceUsed2 = 0, linkResourceUsed3 = 0;
		int[] mapResult1, mapResult2, mapResult3;
		Integer errorcount1 = 0, errorcount2 = 0, errorcount3 = 0;
		Integer currentNodeReq = 0;
		Integer currentLinkReq = 0;
		for (int t = 0; t < delT; t++) {
			for (int scling = 0; scling < delScling; scling++) {

				K = SliceGenerate.Generate(node_req, link_req, nodeBound, linkBound, Kmax);
				mapResult1 = Mapping.process(K, I, H, C1, B1, node_req, link_req); // 方法1
				mapResult2 = CompareMapping.process(K, I, H, C2, B2, node_req, link_req); // 方法2
				mapResult3 = RandomMapping.process(K, I, H, C3, B3, node_req, link_req);
				for (int k = 0; k < K; k++) {
					currentNodeReq += node_req.get(k);
				}

				for (int k = 0; k < K - 1; k++) {
					currentLinkReq += link_req.get(k);
				}
				if (mapResult1 == null) {
					errorcount1++;
				} else {
					nodeResourceUsed1 += mapResult1[0];
					linkResourceUsed1 += mapResult1[1];
					totalResourceUsed1 += mapResult1[2];
				}

				if (mapResult2 == null) {
					errorcount2++;
				} else {
					nodeResourceUsed2 += mapResult2[0];
					linkResourceUsed2 += mapResult2[1];
					totalResourceUsed2 += mapResult2[2];
				}

				if (mapResult3 == null) {
					errorcount3++;
				} else {
					nodeResourceUsed3 += mapResult3[0];
					linkResourceUsed3 += mapResult3[1];
					totalResourceUsed3 += mapResult3[2];
				}

			}

			// 总资源归一化
			result.get(0).add(errorcount1);

			result.get(1).add(errorcount2);

			result.get(2).add(errorcount3);

		}

		return result;
	}
}
