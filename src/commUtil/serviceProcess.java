package commUtil;

import algorithm.CompareMapping;

import java.io.PrintStream;
import java.util.ArrayList;

public class serviceProcess {

	static Integer K = 0;
	static Integer Kmax = 5;
	static Integer I = 10;
	static Integer H = 1000;
	static ArrayList<Integer> C = new ArrayList<Integer>(I * H + 1);
	static int[][] B = new int[I][I];
	static ArrayList<Integer> node_req = new ArrayList<Integer>(Kmax);
	static ArrayList<Integer> link_req = new ArrayList<Integer>(Kmax - 1);

	public static void main(String[] args) throws Exception {
		/**
		 * 重定向到文件
		 */
		PrintStream printStream = new PrintStream("I:\\Google 云端硬盘\\eclipse-workspace\\simulation\\src\\result.txt");
		System.setOut(printStream);
		/**
		 * 数据准备
		 */
		///////////////////////////////////////////////////////////////// 随机数据
		/**
		 * 读取虚拟机容量，并保存在一维数组C
		 */
		// C.add(Integer.MAX_VALUE);
		// FileInputStream f_C = new FileInputStream("I:\\Google
		// 云端硬盘\\eclipse-workspace\\simu\\src\\C.csv");
		// for (int i = 0; i < I; i++) {
		// for (int j = 0; j < H; j++) {
		// C.add(f_C.read());
		// }
		// }
		// f_C.close();
		//
		// /**
		// * 从文件中读取带宽矩阵B
		// */
		// FileInputStream f_B = new FileInputStream("I:\\Google
		// 云端硬盘\\eclipse-workspace\\simu\\src\\B.csv");
		// for (int i = 0; i < I; i++) {
		// for (int j = i; j < I; j++) {
		// B[i][j] = f_B.read();
		// B[j][i] = B[i][j];
		// }
		// }
		// f_B.close();

		///////////////////////////////////////////////////////////////// 随机数据

		/////////////////////// 自定义数据
		int[] cap = { 10, 20, 50, 100, 200, 300, 400, 500, 600, 700 };
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

		/////////////////////// 自定义数据

		/**
		 * 备份初始的资源情况
		 */
		ArrayList<Integer> initC = new ArrayList<Integer>(I * H + 1);
		initC = ((ArrayList<Integer>) C.clone());
		int[][] initB = new int[I][I];
		// initB = B.clone();

		for (int i = 0; i < I; i++) {
			for (int j = 0; j < I; j++) {
				initB[i][j] = B[i][j];
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
		Integer totalResourceUsed = 0;
		Integer nodeResourceUsed = 0;
		Integer linkResourceUsed = 0;
		int[] mapResult;
		Integer errorcount = 0;
		int nodeBound = 700;
		int linkBound = 100;
		for (int iter = 0; iter < 10; iter++) {
			K = SliceGenerate.Generate(node_req, link_req, nodeBound, linkBound, Kmax);
			// dispayInitalInfo();
			// mapResult = Mapping.process(K, I, H, C, B, node_req, link_req); // 方法1
			mapResult = CompareMapping.process(K, I, H, C, B, node_req, link_req); // 方法2
			if (mapResult == null) {
				errorcount++;
			} else {
				nodeResourceUsed += mapResult[0];
				linkResourceUsed += mapResult[1];
				totalResourceUsed += mapResult[2];
			}
		}
		///////////////////////////////////////////////////////////////////////////////////////////
		// /**
		// * 通过另外一种方式计算资源的损失
		// */
		// int checkResourceUsed = 0;
		// for (int i = 1; i < C.size(); i++) {
		// if (C.get(i) != initC.get(i)) {
		// checkResourceUsed += initC.get(i);
		// }
		// }
		// System.out.println();
		// System.out.println("nodeResourceUsed: " + checkResourceUsed);
		//
		// float alpha = 1f;
		// for (int i = 0; i < I; i++) {
		// for (int j = i + 1; j < I; j++) {
		// checkResourceUsed += Math.round(alpha * (initB[i][j] - B[i][j]));
		// }
		// }
		// System.out.println("checkResourceUsed: " + checkResourceUsed);
		///////////////////////////////////////////////////////////////////////////////////////////
		System.out.println("result:---------------------------------------------------------------");
		System.out.println("nodeResourceUsed: " + nodeResourceUsed);
		System.out.println("linkResourceUsed: " + linkResourceUsed);
		System.out.println("totalResourceUsed: " + totalResourceUsed);
		System.out.println("errorcount: " + errorcount);
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
