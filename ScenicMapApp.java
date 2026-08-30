package langkawigraphsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ScenicMapApp extends Application {

    private static final int CANVAS_WIDTH = 600;
    private static final int CANVAS_HEIGHT = 480;
    private static final int CENTER_X = CANVAS_WIDTH / 2;
    private static final int CENTER_Y = CANVAS_HEIGHT / 2;
    private static final int RADIUS = 170;
    private static boolean fxStarted = false;

    // -------------------------------------------------------------------------
    // builds a default network and shows it when run standalone
    // -------------------------------------------------------------------------
    @Override
    public void start(Stage primaryStage) {
        ScenicGraph defaultGraph = new ScenicGraph();
        defaultGraph.createDefaultNetwork();
        buildAndShow(primaryStage, defaultGraph);
    }

    // -------------------------------------------------------------------------
    // opens the graph window from the console app, safe to call again
    // -------------------------------------------------------------------------
    public static void open(ScenicGraph graph) {
        if (!fxStarted) {
            fxStarted = true;
            Platform.startup(() -> {
                Platform.setImplicitExit(false);
                new ScenicMapApp().buildAndShow(new Stage(), graph);
            });
        } else {
            Platform.runLater(() -> new ScenicMapApp().buildAndShow(new Stage(), graph));
        }
    }

    // -------------------------------------------------------------------------
    // builds the window layout (title, refresh button, canvas) and shows it
    // -------------------------------------------------------------------------
    private void buildAndShow(Stage primaryStage, ScenicGraph graph) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label title = new Label("Langkawi SkyCab & Hiking Trail Network");
        title.setFont(Font.font(16));
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> redraw(root, graph));

        HBox header = new HBox(15, title, refreshButton);
        header.setAlignment(Pos.CENTER);
        root.setTop(header);

        redraw(root, graph); // initial draw

        primaryStage.setTitle("Scenic Cable Car and Hiking Trail Network");
        primaryStage.setScene(new Scene(root, CANVAS_WIDTH + 20, CANVAS_HEIGHT + 90));
        primaryStage.show();
    }

    // -------------------------------------------------------------------------
    // re-reads the graph and redraws the canvas and legend
    // -------------------------------------------------------------------------
    private void redraw(BorderPane root, ScenicGraph graph) {
        if (graph.isEmpty()) {
            StackPane empty = new StackPane(new Label("Graph is empty. Add scenic spots in the console app first."));
            empty.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);
            root.setCenter(empty);
            root.setBottom(null);
        } else {
            root.setCenter(buildGraphView(graph));
            root.setBottom(buildLegend());
        }
    }

    // -------------------------------------------------------------------------
    // converts the graph into vertices, edges and a GraphView
    // -------------------------------------------------------------------------
    private GraphView buildGraphView(ScenicGraph graph) {
        List<String> names = new ArrayList<>(graph.getSpotNames());
        int n = names.size();

        ScenicSpot[] vertices = new ScenicSpot[n];
        Map<String, Integer> indexOf = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int[] xy = circlePosition(i, n);
            vertices[i] = new ScenicSpot(names.get(i), xy[0], xy[1]);
            indexOf.put(names.get(i), i);
        }

        List<String[]> routes = graph.getRoutesOnce(); // each {spotA, spotB, routeType}
        int[][] edges = new int[routes.size() * 2][2];
        Map<String, String> routeTypes = new HashMap<>();
        for (int i = 0; i < routes.size(); i++) {
            int u = indexOf.get(routes.get(i)[0]);
            int v = indexOf.get(routes.get(i)[1]);
            String type = routes.get(i)[2];
            edges[i * 2] = new int[]{u, v};
            edges[i * 2 + 1] = new int[]{v, u};
            routeTypes.put(GraphView.edgeKey(u, v), type);
        }

        UnweightedGraph<ScenicSpot> unweightedGraph = new UnweightedGraph<>(vertices, edges);
        GraphView graphView = new GraphView(unweightedGraph, routeTypes);
        graphView.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        graphView.setStyle("-fx-background-color: #f7f8fa; -fx-border-color: #d0d3d8; -fx-border-width: 1;");
        return graphView;
    }

    private int[] circlePosition(int i, int n) {
        if (n == 1) {
            return new int[]{CENTER_X, CENTER_Y};
        }
        double angle = (2 * Math.PI * i / n) - (Math.PI / 2);
        int x = CENTER_X + (int) Math.round(RADIUS * Math.cos(angle));
        int y = CENTER_Y + (int) Math.round(RADIUS * Math.sin(angle));
        return new int[]{x, y};
    }

    // -------------------------------------------------------------------------
    // builds the colour legend
    // -------------------------------------------------------------------------
    private HBox buildLegend() {
        HBox legend = new HBox(15);
        legend.setPadding(new Insets(10, 0, 0, 0));
        legend.setAlignment(Pos.CENTER);
        legend.getChildren().add(legendItem("Cable Car Route", Color.web("#1f77b4")));
        legend.getChildren().add(legendItem("Walking Path", Color.web("#2ca02c")));
        legend.getChildren().add(legendItem("Hiking Trail", Color.web("#d62728")));
        return legend;
    }

    // -------------------------------------------------------------------------
    // builds one coloured line + label for the legend
    // -------------------------------------------------------------------------
    private HBox legendItem(String label, Color color) {
        Line sample = new Line(0, 0, 24, 0);
        sample.setStroke(color);
        sample.setStrokeWidth(3);
        HBox box = new HBox(5, sample, new Label(label));
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
