package langkawigraphsystem;

import java.util.List;
import java.util.Map;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class GraphView extends Pane {
    private Graph<? extends Displayable> graph;

    public GraphView(Graph<? extends Displayable> graph) {
        this(graph, null);
    }

    public GraphView(Graph<? extends Displayable> graph, Map<String, String> routeTypes) {
        this.graph = graph;

        for (int i = 0; i < graph.getSize(); i++) {
            List<Integer> neighbors = graph.getNeighbors(i);
            int x1 = graph.getVertex(i).getX();
            int y1 = graph.getVertex(i).getY();
            for (int v : neighbors) {
                int x2 = graph.getVertex(v).getX();
                int y2 = graph.getVertex(v).getY();

                Line line = new Line(x1, y1, x2, y2);
                if (routeTypes != null) {
                    String type = routeTypes.get(edgeKey(i, v));
                    line.setStroke(colorFor(type));
                    line.setStrokeWidth(2);
                    if ("Hiking Trail".equals(type)) {
                        line.getStrokeDashArray().addAll(6.0, 6.0);
                    }
                } else {
                    line.setStroke(Color.web("#9aa0a6"));
                }
                getChildren().add(line);
            }
        }

        List<? extends Displayable> vertices = graph.getVertices();
        for (int i = 0; i < graph.getSize(); i++) {
            int x = vertices.get(i).getX();
            int y = vertices.get(i).getY();
            String name = vertices.get(i).getName();

            Circle circle = new Circle(x, y, 16);
            circle.setFill(Color.CORNFLOWERBLUE);
            circle.setStroke(Color.DARKBLUE);
            circle.setStrokeWidth(1.5);
            getChildren().add(circle);

            Text label = new Text(x - name.length() * 3.0, y - 24, name);
            label.setStyle("-fx-font-weight: bold;");
            getChildren().add(label);
        }
    }

    public static String edgeKey(int u, int v) {
        return Math.min(u, v) + "-" + Math.max(u, v);
    }

    private static Color colorFor(String routeType) {
        if (routeType == null) {
            return Color.BLACK;
        }
        switch (routeType) {
            case "Cable Car Route":
                return Color.web("#1f77b4"); // blue
            case "Walking Path":
                return Color.web("#2ca02c"); // green
            case "Hiking Trail":
                return Color.web("#d62728"); // red
            default:
                return Color.BLACK;
        }
    }
}
