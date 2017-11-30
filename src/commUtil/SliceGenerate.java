package commUtil;

import java.util.ArrayList;
import java.util.Random;

public class SliceGenerate {
	public static Integer Generate(ArrayList<Integer> node_req, ArrayList<Integer> link_req, int nodeBound,
			int linkBound, Integer Kmax) {
		Random random = new Random();
		Integer K = random.nextInt(Kmax - 2) + 2;
		for (int k = 0; k < K; k++) {
			node_req.set(k, random.nextInt(nodeBound) + 1);
		}
		// Collections.sort(node_req);
		for (int k = 0; k < K - 1; k++) {
			link_req.set(k, random.nextInt(linkBound) + 1);
		}
		return K;
	}
}
