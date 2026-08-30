package langkawigraphsystem;

import java.util.*;

public class UnweightedGraph<V> extends AbstractGraph<V> {

    public UnweightedGraph() {
    }

    public UnweightedGraph(V[] vertices, int[][] edges) {
        super(vertices, edges);
    }

    public UnweightedGraph(List<V> vertices, List<AbstractGraph.Edge> edges) {
        super(vertices, edges);
    }

    public UnweightedGraph(List<AbstractGraph.Edge> edges, int numberOfVertices) {
        super(edges, numberOfVertices);
    }

    public UnweightedGraph(int[][] edges, int numberOfVertices) {
        super(edges, numberOfVertices);
    }
}
