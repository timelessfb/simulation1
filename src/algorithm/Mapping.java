package algorithm;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commUtil.MinCostFlowSimple;
import commUtil.dp_node;
import commUtil.MinCostFlowSimple.Edge;

/**
 * 
 * @question:多背包问题，节点映射的排序问题
 * @question:B矩阵的对角线带宽
 * @question:MCF是否可以处理同一台主机之间的传输
 * @question:处理失败的解决方式
 * @question:参数设置
 * @author fb
 *
 */
public class Mapping {

	/**
	 * 测试
	 */
	public void test() throws Exception {
		/**
		 * 数据准备
		 */
		PrintStream printStream = new PrintStream(
				"I:\\\\Google 云端硬盘\\\\eclipse-workspace\\\\simu\\\\src\\\\systemlog.txt");
		System.setOut(printStream);
		Integer K = 3;
		Integer I = 10;
		Integer H = 10;
		/**
		 * 读取虚拟机容量，并保存在一维数组C
		 */
		ArrayList<Integer> C = new ArrayList<Integer>(I * H + 1);
		C.add(Integer.MAX_VALUE);
		FileInputStream f_C = new FileInputStream("I:\\Google 云端硬盘\\eclipse-workspace\\simu\\src\\C.csv");
		for (int i = 0; i < I; i++) {
			for (int j = 0; j < H; j++) {
				C.add(f_C.read());
			}
		}
		f_C.close();

		/**
		 * 从文件中读取带宽矩阵B
		 */
		FileInputStream f_B = new FileInputStream("I:\\Google 云端硬盘\\eclipse-workspace\\simu\\src\\B.csv");
		int[][] B = new int[I][I];
		for (int i = 0; i < I; i++) {
			for (int j = i; j < I; j++) {
				B[i][j] = f_B.read();
				B[j][i] = B[i][j];
			}
		}
		f_B.close();

		/**
		 * 读取虚拟节点的请求参数node_req
		 */
		FileInputStream f_node_req = new FileInputStream("I:\\Google 云端硬盘\\eclipse-workspace\\simu\\src\\node_req.csv");
		ArrayList<Integer> node_req = new ArrayList<Integer>(K);
		for (Integer i = 0; i < K; i++) {
			node_req.add(f_node_req.read());
		}
		f_node_req.close();

		/**
		 * 读取虚拟链路的请求参数link_req
		 */
		FileInputStream f_link_req = new FileInputStream("I:\\Google 云端硬盘\\eclipse-workspace\\simu\\src\\link_req.csv");
		ArrayList<Integer> link_req = new ArrayList<Integer>(K - 1);
		for (int k = 0; k < K - 1; k++) {
			link_req.add(k, f_link_req.read());
		}
		f_link_req.close();

		/**
		 * 打印初始信息
		 */
		System.out.println("初始信息：");
		System.out.println("initial node cap C: ");
		for (int i = 1; i < C.size(); i++) {
			System.out.printf("%10d ", C.get(i));
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
		for (int i = 0; i < node_req.size(); i++) {
			System.out.printf("%10d ", node_req.get(i));
		}
		System.out.println();

		System.out.println("link req: ");
		for (int i = 0; i < link_req.size(); i++) {
			System.out.printf("%10d", link_req.get(i));
		}
		System.out.println();

		/**
		 * 映射
		 */
		Integer totalResourceUsed = 0;
		int result[] = process(K, I, H, C, B, node_req, link_req);
		totalResourceUsed = result[2];
		if (totalResourceUsed != null) {
			System.out.println("totalResourceUsed: " + totalResourceUsed);

		}
	}

	public static int[] process(Integer K, Integer I, Integer H, ArrayList<Integer> C, int[][] B,
			ArrayList<Integer> node_req, ArrayList<Integer> link_req) {

		/**
		 * 记录资源初始情况
		 */
		ArrayList<Integer> initC = (ArrayList<Integer>) C.clone();
		int[][] initB = new int[I][I];
		for (int i = 0; i < I; i++) {
			for (int j = 0; j < I; j++) {
				initB[i][j] = B[i][j];
			}
		}

		/**
		 * 节点映射
		 */
		ArrayList<ArrayList<Integer>> nodeMapSolution = nodeMap(K, C, node_req);
		if (nodeMapSolution == null) {
			System.out.println("");
			System.out.println("DP节点映射失败");
			System.out.println("");
			return null;
		}
		// System.out.println("this is node mapping solution: ");
		// System.out.println("nodeMapSolution: " + nodeMapSolution);

		/**
		 * 链路映射
		 */
		int[][] linkMapSolution = linkMap(K, I, H, initC, B, link_req, nodeMapSolution);
		if (linkMapSolution == null) {
			System.out.println("");
			System.out.println("myalg链路映射失败");
			// 回滚节点的资源
			for (int i = 0; i < I * H; i++) {
				C.set(i, initC.get(i));
			}
			System.out.println("");
			return null;
		}

		// System.out.println("this is link mapping solution: ");
		// for (int i = 0; i < I; i++) {
		// System.out.println();
		// for (int j = 0; j < I; j++) {
		// System.out.printf("%10d ", linkMapSolution[i][j]);
		// }
		// }

		/**
		 * 计算资源的使用情况
		 */
		/**
		 * 计算资源的总的使用情况
		 */
		Integer nodeResourceUsed = 0;
		Integer linkResourceUsed = 0;
		Integer totalResourceUsed = 0;

		// 节点资源计算
		for (int i = 0; i < nodeMapSolution.size(); i++) {
			for (int j = 0; j < nodeMapSolution.get(i).size(); j++) {
				nodeResourceUsed += initC.get(nodeMapSolution.get(i).get(j));
			}
		}

		// System.out.println();
		// System.out.println("node resourceUsed: " + nodeResourceUsed);

		for (int i = 0; i < I; i++) {
			for (int j = i + 1; j < I; j++) {
				linkResourceUsed += initB[i][j] - B[i][j];
			}
		}
		// System.out.println(B);
		// System.out.println(initB);
		// System.out.println(C);
		totalResourceUsed = nodeResourceUsed + linkResourceUsed;
		// System.out.println();
		// System.out.println("nodeResourceUsed: " + nodeResourceUsed);
		// System.out.println("linkResourceUsed: " + linkResourceUsed);
		// System.out.println("totalResourceUsed: " + totalResourceUsed);
		// return totalResourceUsed;
		int[] result = new int[3];
		result[0] = nodeResourceUsed;
		result[1] = linkResourceUsed;
		result[2] = totalResourceUsed;
		return result;
	}

	/**
	 * 节点映射
	 * 
	 * @param K
	 * @param C
	 * @param node_req
	 */
	public static ArrayList<ArrayList<Integer>> nodeMap(Integer K, ArrayList<Integer> C, ArrayList<Integer> node_req) {
		/**
		 * 节点映射
		 */
		ArrayList<Integer> CC = (ArrayList<Integer>) C.clone();
		ArrayList<Integer> nodemapresult = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> nodeMapSolution = new ArrayList<ArrayList<Integer>>();// 记录映射结果{D_k}
		Integer currentResourceUnUsed = 0;
		for (Integer i = 0; i < K; i++) {
			currentResourceUnUsed = 0;
			Integer req = node_req.get(i);
			Integer bigreq = node_req.get(i);
			// 判断现有的资源是否能够完成当前节点的映射
			for (Integer c : CC) {
				if (c < Integer.MAX_VALUE) {
					currentResourceUnUsed += c;// 计算现有的未使用节点资源的总量
				}
			}
			if (currentResourceUnUsed < req) {
				// 此时还没有修改现有资源C，故在调用者一方不用回滚
				System.out.println("DP节点映射失败：资源不够用");
				return null;
			}
			while (true) {
				nodemapresult = dp_node.KnapsackDP(CC, CC, bigreq, CC.size() - 1, req);
				if (nodemapresult != null) {
					break;
				} else {
					bigreq++;
					// System.out.println("don't find");
				}
			}

			nodeMapSolution.add((ArrayList<Integer>) nodemapresult.clone());
			for (int j = 0; j < nodemapresult.size(); j++) {
				CC.set(nodemapresult.get(j), Integer.MAX_VALUE);
			}
		}
		// 修改现有资源
		for (int i = 0; i < C.size(); i++) {
			C.set(i, CC.get(i));
		}
		return nodeMapSolution;
	}

	public static int[][] linkMap(Integer K, Integer I, Integer H, ArrayList<Integer> C, int[][] B,
			ArrayList<Integer> link_req, ArrayList<ArrayList<Integer>> nodeMapSolution) {
		/**
		 * 加上两个虚拟节点
		 */
		int[][] BB = new int[I + 2][I + 2];
		for (int i = 0; i < I; i++) {
			for (int j = 0; j < I; j++) {
				BB[i][j] = B[i][j];
			}
		}
		int[][] linkMapSolution = new int[I][I];

		/**
		 * 分段映射逻辑链路需求
		 */
		Integer r_kl;
		Integer del;// 由于精度导致的最大误差允许度
		Integer real_req;
		ArrayList<Integer> pre_node_k;
		ArrayList<Integer> neighbor_node_l;
		HashMap<Integer, Integer> D_k = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> D_l = new HashMap<Integer, Integer>();
		// 分別映射几条虚拟链路的需求
		for (int k = 0; k < K - 1; k++) {
			del = 0;
			D_k.clear();
			D_l.clear();
			r_kl = link_req.get(k);// 获得当前的总的带宽请求
			pre_node_k = nodeMapSolution.get(k);
			neighbor_node_l = nodeMapSolution.get(k + 1);
			Integer index_of_vm, index_of_server;

			/**
			 * 计算D_k,D_l
			 */
			for (int i = 0; i < pre_node_k.size(); i++) {
				index_of_vm = pre_node_k.get(i);
				index_of_server = (index_of_vm - 1) / H;
				if (D_k.containsKey(index_of_server)) {
					D_k.replace(index_of_server, D_k.get(index_of_server) + C.get(index_of_vm));
				} else {
					D_k.put(index_of_server, C.get(index_of_vm));
				}
			}

			for (int i = 0; i < neighbor_node_l.size(); i++) {
				index_of_vm = neighbor_node_l.get(i);
				index_of_server = (index_of_vm - 1) / H;
				if (D_l.containsKey(index_of_server)) {
					D_l.replace(index_of_server, D_l.get(index_of_server) + C.get(index_of_vm));
				} else {
					D_l.put(index_of_server, C.get(index_of_vm));
				}
			}

			/**
			 * 调用最小费用最大流解决D_k到D_l的链路映射
			 */

			// 设置增加了两个超级节点I,I+1的带宽矩阵
			Collection<Integer> values;
			values = D_k.values();
			Integer sum = 0;
			for (Integer value : values) {
				sum += value;
			}
			for (Map.Entry<Integer, Integer> Entry : D_k.entrySet()) {
				BB[Entry.getKey()][I] = Math.round((float) (r_kl * Entry.getValue()) / sum);// 计算每个服务器的输出带宽
				BB[I][Entry.getKey()] = BB[Entry.getKey()][I];
			}

			values = D_l.values();
			sum = 0;
			for (Integer value : values) {
				sum += value;
			}
			for (Map.Entry<Integer, Integer> Entry : D_l.entrySet()) {
				BB[Entry.getKey()][I + 1] = Math.round((float) (r_kl * Entry.getValue()) / sum);// 计算每个服务器的输入带宽
				BB[I + 1][Entry.getKey()] = BB[Entry.getKey()][I + 1];
			}

			real_req = 0;
			del = 0;
			for (int i = 0; i < I; i++) {
				real_req += BB[i][I];
			}
			for (int i = 0; i < I; i++) {
				del += BB[i][I] - BB[i][I + 1];
			}
			if (del > 0) {
				real_req -= del;
			}

			List<Edge>[] graph = MinCostFlowSimple.AdjtoGraph(BB, I + 2);
			int[] minCostFlow = MinCostFlowSimple.minCostFlow(graph, I, I + 1, Integer.MAX_VALUE, BB);

			if (minCostFlow[0] < real_req) {
				// 还没有修改链路资源B，所以调用一方无需回滚
				// System.out.println("映射链路资源" + minCostFlow[0] + "实际需要的资源" + r_kl);
				return null;
			}

			for (int i = 0; i < I + 2; i++) {
				BB[i][I] = 0;
				BB[i][I + 1] = 0;
				BB[I][i] = 0;
				BB[I + 1][i] = 0;
			}

			// System.out.println("*********");
		}

		for (int i = 0; i < I; i++) {
			for (int j = 0; j < I; j++) {
				linkMapSolution[i][j] = B[i][j] - BB[i][j];
			}
		}

		for (int i = 0; i < I; i++) {
			for (int j = 0; j < I; j++) {
				B[i][j] = BB[i][j];
			}
		}
		return linkMapSolution;
	}
}
