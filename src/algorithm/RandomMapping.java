package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import commUtil.ShortPath;
import commUtil.ShortPath.Edge;

public class RandomMapping {
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
		ArrayList<Integer> nodeMapSolution = nodeMap(K, C, node_req, I, H);
		if (nodeMapSolution == null) {
			System.out.println("");
			System.out.println("算法3节点映射失败");
			System.out.println("");
			return null;
		}
		// System.out.println("this is node mapping solution: ");
		// System.out.println("nodeMapSolution: " + nodeMapSolution);

		/**
		 * 链路映射
		 */
		int[][] linkMapSolution = linkMap(K, I, H, B, link_req, nodeMapSolution);
		if (linkMapSolution == null) {
			System.out.println("");
			System.out.println("算法3链路映射失败");
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
			nodeResourceUsed += initC.get(nodeMapSolution.get(i));
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
		int[] result = new int[3];
		result[0] = nodeResourceUsed;
		result[1] = linkResourceUsed;
		result[2] = totalResourceUsed;
		return result;

	}

	/**
	 * 
	 * @param K
	 * @param C
	 * @param node_req
	 * @return：映射成功，返回映射方案，否则，返回null
	 */
	public static ArrayList<Integer> nodeMap(Integer K, ArrayList<Integer> C, ArrayList<Integer> node_req, Integer I,
			Integer H) {
		ArrayList<Integer> CC = new ArrayList<Integer>();
		CC = (ArrayList<Integer>) C.clone();
		ArrayList<Integer> nodeMapSolution = new ArrayList<Integer>();
		Random random = new Random();
		Integer bound = I;
		for (int k = 0; k < K; k++) { // 初始化
			nodeMapSolution.add(0);
		}
		for (int k = 0; k < K; k++) {
			for (int j = 0; j < CC.size(); j++) {
				// 确定是否有可映射下去
				if (CC.get(j) != Integer.MAX_VALUE && CC.get(j) >= node_req.get(k)) {
					nodeMapSolution.set(k, j);
					break;
				}
			}

			if (nodeMapSolution.get(k) == 0) {
				return null; // 映射失败
			} else {
				// 完成映射
				while (true) {
					int j = (random.nextInt(5 * bound)) % I;//////////////////////// 参数3可以调整
					for (int h = 0; h < H; h++) {
						if (CC.get(j * H + 1 + h) != Integer.MAX_VALUE && CC.get(j * H + 1 + h) >= node_req.get(k)) {
							nodeMapSolution.set(k, j * H + 1 + h);
							CC.set(j * H + 1 + h, Integer.MAX_VALUE);
							break;
						}
						// 此服务器j无法映射
						if (CC.get(j * H + 1 + h) != Integer.MAX_VALUE && CC.get(j * H + 1 + h) < node_req.get(k)) {
							break;
						}
					}
					// 判断是否需要换个服务器映射
					if ((nodeMapSolution.get(k) - 1) / H == j) {// 此时代表映射成功
						break;
					}
				}
			}
		}
		// 修改现有资源
		for (int i = 0; i < C.size(); i++) {
			C.set(i, CC.get(i));
		}
		return nodeMapSolution;
	}

	/**
	 * 
	 * @param K
	 * @param I
	 * @param H
	 * @param C
	 * @param B
	 * @param link_req
	 * @param nodeMapSolution
	 * @return
	 */
	public static int[][] linkMap(Integer K, Integer I, Integer H, int[][] B, ArrayList<Integer> link_req,
			ArrayList<Integer> nodeMapSolution) {
		int[][] linkMapSolution = new int[I][I];
		Integer r_kl;
		Integer pre_node;
		Integer neighbor_node;
		Integer edge_start_node;
		Integer edge_end_node;
		List<Edge>[] edges = new List[I];
		int[] dist = new int[I];// 保存源点到各个点的最短距离
		int[] pred = new int[I];// 保存前驱节点
		int[][] BB = new int[I][I];
		for (int i = 0; i < I; i++) {
			for (int j = 0; j < I; j++) {
				BB[i][j] = B[i][j];
			}
		}
		/**
		 * 分別映射几条虚拟链路的需求
		 */
		for (int k = 0; k < K - 1; k++) {
			r_kl = link_req.get(k);
			pre_node = (nodeMapSolution.get(k) - 1) / H;
			neighbor_node = (nodeMapSolution.get(k + 1) - 1) / H;
			/**
			 * 求最短路
			 */
			for (int i = 0; i < I; i++) { // 邻接矩阵（代价矩阵）转换为List<Edge>
				edges[i] = new ArrayList<>();
				for (int j = 0; j < I; j++) {
					if (BB[i][j] > r_kl) {
						edges[i].add(new Edge(j, 1));
					}
				}
			}
			// if (pre_node == neighbor_node) {
			// continue;
			// }

			ShortPath.shortestPaths(edges, pre_node, dist, pred);
			/**
			 * 求到点neighbor_node的最短路径，并在相应的边上减去带宽请求
			 */
			if (dist[neighbor_node] == Integer.MAX_VALUE) {
				return null;// 只要有一条虚拟链路请求没有映射成功，则返回
			}

			edge_end_node = neighbor_node;
			edge_start_node = pred[edge_end_node];
			while (true) {
				if (edge_start_node == -1) {
					break;
				} else {
					BB[edge_start_node][edge_end_node] -= r_kl;
					BB[edge_end_node][edge_start_node] -= r_kl;
					edge_end_node = edge_start_node;
					edge_start_node = pred[edge_end_node];
				}
			}

		}

		/**
		 * 全部映射成功，修改带宽矩阵B
		 */

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
