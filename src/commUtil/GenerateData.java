package commUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

public class GenerateData {
	Integer K = 3;
	Integer I = 10;
	Integer H = 10;

	/**
	 * 
	 * @param I：服务器个数
	 * @param H：每个服务器上虚拟机个数（最大）
	 * @throws Exception
	 */
	public void GenerateC(Integer I, Integer H) throws Exception {
		Random random = new Random();
		FileOutputStream f_C = new FileOutputStream("I:\\Google 云端硬盘\\eclipse-workspace\\simu\\src\\C.csv");
		Integer bound = 200;
		for (int i = 0; i < I; i++) {
			for (int h = 0; h < H; h++) {
				f_C.write(random.nextInt(bound));
			}
		}
		f_C.close();
	}

	public void test_C() throws Exception {
		GenerateC(I, H);
		FileInputStream f_C = new FileInputStream("I:\\Google 云端硬盘\\eclipse-workspace\\simu\\src\\C.csv");
		int[][] C = new int[I][H];
		for (int i = 0; i < I; i++) {
			for (int j = 0; j < H; j++) {
				C[i][j] = f_C.read();
			}
		}
		f_C.close();
		System.out.println("////");
		for (int i = 0; i < I; i++) {
			System.out.println();
			for (int j = 0; j < H; j++) {
				System.out.print(C[i][j] + "*");
			}
		}
		System.out.println("////");
	}

	/**
	 * 
	 * @param I:服务器个数
	 * @throws Exception
	 */
	public void GenerateB(Integer I) throws Exception {
		Random random = new Random();
		Integer bound = 2000;
		FileOutputStream f_B = new FileOutputStream("I:\\Google 云端硬盘\\eclipse-workspace\\simu\\src\\B.csv");
		for (int i = 0; i < I; i++) {
			for (int j = i; j < I; j++) {
				f_B.write(random.nextInt(bound));
			}
		}
		f_B.close();
	}

	public void test_B() throws Exception {
		GenerateB(I);
		FileInputStream f_B = new FileInputStream("I:\\Google 云端硬盘\\eclipse-workspace\\simu\\src\\B.csv");
		int[][] B = new int[I][I];
		for (int i = 0; i < I; i++) {
			for (int j = i; j < I; j++) {
				B[i][j] = f_B.read();
				B[j][i] = B[i][j];
			}
		}
		f_B.close();

		for (int i = 0; i < I; i++) {
			System.out.println();
			for (int j = 0; j < I; j++) {
				System.out.print(B[i][j] + " ");
			}
		}
	}

	/**
	 * 
	 * @param K：逻辑节点个数
	 * @throws Exception
	 */
	public void GenerateLinkReq(Integer K) throws Exception {
		Random random = new Random();
		int bound = 200;
		FileOutputStream f_link_req = new FileOutputStream(
				"I:\\Google 云端硬盘\\eclipse-workspace\\simu\\src\\link_req.csv");
		for (int k = 0; k < K - 1; k++) {
			f_link_req.write(random.nextInt(bound));
		}
		f_link_req.close();
	}

	public void testLinkReq() throws Exception {
		GenerateLinkReq(K);
		FileInputStream f_link_req = new FileInputStream("I:\\Google 云端硬盘\\eclipse-workspace\\simu\\src\\link_req.csv");
		int[] link_req = new int[K - 1];
		for (int k = 0; k < K - 1; k++) {
			link_req[k] = f_link_req.read();
		}
		f_link_req.close();

		for (int i = 0; i < link_req.length; i++) {
			System.out.println(link_req[i]);
		}
	}

	/**
	 * 
	 * @param K：逻辑节点个数
	 * @throws Exception
	 */
	public void GenerateNodeReq(Integer K) throws Exception {
		Random random = new Random();
		int bound = 200;
		FileOutputStream f_node_req = new FileOutputStream(
				"I:\\Google 云端硬盘\\eclipse-workspace\\simu\\src\\node_req.csv");
		for (int k = 0; k < K; k++) {
			f_node_req.write(random.nextInt(bound));
		}
		f_node_req.close();
	}

	public void testNodeReq() throws Exception {
		GenerateNodeReq(K);
		FileInputStream f_node_req = new FileInputStream("I:\\Google 云端硬盘\\eclipse-workspace\\simu\\src\\node_req.csv");
		int[] node_req = new int[K];
		for (int k = 0; k < K; k++) {
			node_req[k] = f_node_req.read();
		}
		f_node_req.close();

		for (int i = 0; i < node_req.length; i++) {
			System.out.println(node_req[i]);
		}
	}
}
