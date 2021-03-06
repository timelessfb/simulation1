package commUtil;

import java.util.ArrayList;
import java.util.Random;

public class SliceGenerate {
    public static Integer Generate(ArrayList<Integer> node_req, ArrayList<Integer> link_req, int nodeBound,
                                   int linkBound, Integer Kmax) {
        int m = 1;
        Random random = new Random();
        random.nextGaussian();
        Integer K = random.nextInt(Kmax - 2) + 2;
        Double variance = (double) 30f;
        Double mean = (double) 100;
        for (int k = 0; k < K; k++) {
//            node_req.set(k, random.nextInt(nodeBound * m) % nodeBound + 1);
//            random.nextGaussian()*variance+mean
//            Math.abs((int)(random.nextGaussian()*variance+mean))% nodeBound+1;
            node_req.set(k, Math.abs((int) (random.nextGaussian() * variance + mean)));
//            System.out.println(node_req.get(k));
        }
        // Collections.sort(node_req);
        for (int k = 0; k < K - 1; k++) {
            link_req.set(k, random.nextInt(linkBound * m) % linkBound + 1);
        }
        return K;
    }
}
