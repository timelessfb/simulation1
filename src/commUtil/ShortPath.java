package commUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class ShortPath {

    /**
     * @param edges：边集
     * @param s：源点
     * @param prio：最短距离数组
     * @param pred：前去节点数组
     * @note：可以处理负代价边
     */
    public static void shortestPaths(List<Edge>[] edges, int s, int[] prio, int[] pred) {
        Arrays.fill(pred, -1);
        Arrays.fill(prio, Integer.MAX_VALUE);
        prio[s] = 0;
        PriorityQueue<Long> q = new PriorityQueue<>();
        q.add((long) s);
        while (!q.isEmpty()) {
            long cur = q.remove();
            int curu = (int) cur;
            if (cur >>> 32 != prio[curu]) {
                continue;
            }
            for (Edge e : edges[curu]) {
                int v = e.t;
                int nprio = prio[curu] + e.cost;
                if (prio[v] > nprio) {
                    prio[v] = nprio;
                    pred[v] = curu;
                    q.add(((long) nprio << 32) + v);
                }
            }
        }
    }

    /**
     * @author fb
     * @see：边类定义
     */
    public static class Edge {
        int t, cost;

        public Edge(int t, int cost) {
            this.t = t;
            this.cost = cost;
        }
    }

    public void test() {
        Integer inf = 0;
        // int[][] cost = { { inf, 7, 9, inf, inf, 14 }, { 7, inf, 10, 15, inf, inf }, {
        // 9, 10, inf, 11, inf, 2 },
        // { inf, 15, 11, inf, 6, inf }, { inf, inf, inf, 6, inf, 9 }, { 14, inf, 2,
        // inf, 9, inf } }; // 邻接矩阵（代价矩阵）

        int[][] cost = {{inf, 1, inf}, {1, inf, inf}, {inf, inf, inf}};
        int n = cost.length;
        List<Edge>[] edges = new List[n];
        for (int i = 0; i < n; i++) { // 邻接矩阵（代价矩阵）转换为List<Edge>
            edges[i] = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (cost[i][j] != inf) {
                    edges[i].add(new Edge(j, cost[i][j]));
                }
            }
        }
        int[] dist = new int[n];// 保存源点到各个点的最短距离
        int[] pred = new int[n];// 保存前驱节点
        shortestPaths(edges, 0, dist, pred);
        /**
         * 输出到特定点的最短路 0-1
         */
        System.out.println("最短距离");
        for (int i = 0; i < dist.length; i++) {
            System.out.print(dist[i] + ",");
        }
        System.out.println();
        System.out.println("前驱节点");
        for (int i = 0; i < pred.length; i++) {
            System.out.print(pred[i] + ",");
        }
    }
}