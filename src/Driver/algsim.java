package Driver;

import algorithm.CompareMapping;
import algorithm.Mapping;
import commUtil.SliceGenerate;

import java.io.PrintStream;
import java.util.ArrayList;

public class algsim {

	static Integer K = 0;
	static Integer Kmax = 5;
	static Integer I = 5;
	static Integer H = 10000;
	static ArrayList<Integer> C = new ArrayList<Integer>(I * H + 1);
	static int[][] B = new int[I][I];
	static ArrayList<Integer> node_req = new ArrayList<Integer>(Kmax);
	static ArrayList<Integer> link_req = new ArrayList<Integer>(Kmax - 1);

	public static void main(String[] args) throws Exception {
		PrintStream printStream = new PrintStream("I:\\Google 云端硬盘\\eclipse-workspace\\simulation\\src\\result.txt");
		System.setOut(printStream);
		// int[] cap = { 10, 20, 50, 100, 200, 300, 400, 500, 600, 700 };
		int[] cap = { 4, 9, 16, 25, 36, 49, 81, 100, 150, 200 };
		C.add(Integer.MAX_VALUE);
		for (int i = 0; i < I; i++) {
			for (int j = 0; j < H; j++) {
				C.add(cap[9 - i]);
			}
		}

		for (int i = 0; i < I; i++) {
			for (int j = i; j < I; j++) {
				B[i][j] = Integer.MAX_VALUE - 1;
				B[j][i] = B[i][j];
			}
		}

		B[0][1] = 500;
		B[1][0] = 500;

		ArrayList<Integer> C1 = new ArrayList<Integer>(I * H + 1);
		C1 = ((ArrayList<Integer>) C.clone());
		ArrayList<Integer> C2 = new ArrayList<Integer>(I * H + 1);
		C2 = ((ArrayList<Integer>) C.clone());
		int[][] B1 = new int[I][I];
		int[][] B2 = new int[I][I];

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
		Integer totalResourceUsed1 = 0, totalResourceUsed2 = 0;
		Integer nodeResourceUsed1 = 0, nodeResourceUsed2 = 0;
		Integer linkResourceUsed1 = 0, linkResourceUsed2 = 0;
		int[] mapResult1, mapResult2;
		Integer errorcount1 = 0, errorcount2 = 0;
		int nodeBound = 100;
		int linkBound = 50;
		Integer R = 100;
		for (int iter = 0; iter < R; iter++) {
			K = SliceGenerate.Generate(node_req, link_req, nodeBound, linkBound, Kmax);
			// System.out.println();
			// System.out.println("node_req" + node_req);
			// System.out.println("link_req" + link_req);
			// System.out.println();
			// dispayInitalInfo();
			mapResult1 = Mapping.process(K, I, H, C1, B1, node_req, link_req); // 方法1
			mapResult2 = CompareMapping.process(K, I, H, C2, B2, node_req, link_req); // 方法2
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
		}
		System.out.println();
		System.out.println("result1:---------------------------------------------------------------");
		System.out.println("nodeResourceUsed: " + nodeResourceUsed1);
		System.out.println("linkResourceUsed: " + linkResourceUsed1);
		System.out.println("totalResourceUsed: " + totalResourceUsed1);
		System.out.println("errorcount: " + errorcount1);
		System.out.println("result2:---------------------------------------------------------------");
		System.out.println("nodeResourceUsed: " + nodeResourceUsed2);
		System.out.println("linkResourceUsed: " + linkResourceUsed2);
		System.out.println("totalResourceUsed: " + totalResourceUsed2);
		System.out.println("errorcount: " + errorcount2);
		System.out.println("result3:---------------------------------------------------------------");
		System.out.println(
				"avggain: " + ((totalResourceUsed2 / (R - errorcount2)) - (totalResourceUsed1) / (R - errorcount1)));
	}

	public static void dispayInitalInfo() {
		/**
		 * 打印初始信息
		 */
		System.out.println();
		System.out.println("初始信息：");
		System.out.println("initial node cap C: ");
		for (int i = 1; i < C.size(); i++) {
			if (C.get(i) == Integer.MAX_VALUE) {
				System.out.printf("%10d ", 0);
			} else {
				System.out.printf("%10d ", C.get(i));
			}
		}
		System.out.println();
		System.out.println();

		System.out.println("initial link bandwith B: ");
		for (int i = 0; i < I; i++) {
			System.out.println();
			for (int j = 0; j < I; j++) {
				System.out.printf("%10d ", B[i][j]);
			}
		}
		System.out.println();

		System.out.println("node req: ");
		for (int i = 0; i < K; i++) {
			System.out.printf("%10d ", node_req.get(i));
		}
		System.out.println();

		System.out.println("link req: ");
		for (int i = 0; i < K - 1; i++) {
			System.out.printf("%10d", link_req.get(i));
		}
		System.out.println();
	}

}
