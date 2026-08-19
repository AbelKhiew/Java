/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */
import java.util.*;

/**
 * AMCS2034 Introduction to Data Structures and Algorithms
 * System: Langkawi SkyCab and Hiking Trail Network System
 * Representation: Adjacency List (Undirected Graph)
 * Traversal: Breadth-First Search (BFS)
 */

// Edge class representing connected routes
class Edge {
    private String destination;
    private String routeType;

    public Edge(String destination, String routeType) {
        this.destination = destination;
        this.routeType = routeType;
    }

    public String getDestination() {
        return destination;
    }

    public String getRouteType() {
        return routeType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        return destination.equalsIgnoreCase(edge.destination);
    }

    @Override
    public int hashCode() {
        return destination.toLowerCase().hashCode();
    }
}

// Graph class managing vertices and edges via Adjacency List
class ScenicGraph {
    private Map<String, List<Edge>> adjacencyList;

    public ScenicGraph() {
        this.adjacencyList = new LinkedHashMap<>();
    }

    // Initialize 10 default spots and 11 connections as defined in assignment specifications
    public void createDefaultNetwork() {
        adjacencyList.clear();

        String[] spots = {
            "Oriental Village", "Base Station", "Middle Station", "Top Station",
            "Sky Bridge", "Machinchang Peak", "Seven Wells Waterfall",
            "Jungle Trail", "Viewing Platform", "Sky Bistro"
        };
        for (String spot : spots) {
            addScenicSpot(spot, false);
        }

        addRoute("Oriental Village", "Base Station", "Cable Car Route", false);
        addRoute("Oriental Village", "Sky Bistro", "Walking Path", false);
        addRoute("Base Station", "Middle Station", "Cable Car Route", false);
        addRoute("Middle Station", "Top Station", "Cable Car Route", false);
        addRoute("Middle Station", "Seven Wells Waterfall", "Hiking Trail", false);
        addRoute("Top Station", "Sky Bridge", "Cable Car Route", false);
        addRoute("Sky Bridge", "Machinchang Peak", "Hiking Trail", false);
        addRoute("Machinchang Peak", "Viewing Platform", "Walking Path", false);
        addRoute("Seven Wells Waterfall", "Jungle Trail", "Hiking Trail", false);
        addRoute("Seven Wells Waterfall", "Viewing Platform", "Hiking Trail", false);
        addRoute("Viewing Platform", "Sky Bistro", "Walking Path", false);

        System.out.println("\n[SUCCESS] Default Langkawi SkyCab Network created with 10 spots and 11 routes.");
    }

    public String getCanonicalName(String name) {
        if (name == null) return null;
        for (String key : adjacencyList.keySet()) {
            if (key.equalsIgnoreCase(name.trim())) {
                return key;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return adjacencyList.isEmpty();
    }

    public int getSpotCount() {
        return adjacencyList.size();
    }

    // 1. Add Scenic Spot (Vertex)
    public boolean addScenicSpot(String spotName, boolean printMessage) {
        if (spotName == null || spotName.trim().isEmpty()) {
            if (printMessage) System.out.println("[ERROR] Scenic spot name cannot be empty.");
            return false;
        }

        String canonical = getCanonicalName(spotName);
        if (canonical != null) {
            if (printMessage) System.out.println("[ERROR] Scenic spot '" + canonical + "' already exists in the network.");
            return false;
        }

        String formattedName = formatName(spotName.trim());
        adjacencyList.put(formattedName, new ArrayList<>());
        if (printMessage) {
            System.out.println("[SUCCESS] Scenic spot '" + formattedName + "' added successfully.");
        }
        return true;
    }

    // 2. Remove Scenic Spot (Vertex)
    public boolean removeScenicSpot(String spotName) {
        String canonical = getCanonicalName(spotName);
        if (canonical == null) {
            System.out.println("[ERROR] Scenic spot '" + spotName + "' does not exist in the network.");
            return false;
        }

        adjacencyList.remove(canonical);

        for (List<Edge> neighbors : adjacencyList.values()) {
            neighbors.removeIf(edge -> edge.getDestination().equalsIgnoreCase(canonical));
        }

        System.out.println("[SUCCESS] Scenic spot '" + canonical + "' and its routes were removed successfully.");
        return true;
    }

    // 3. Add Route (Edge)
    public boolean addRoute(String locA, String locB, String routeType, boolean printMessage) {
        String canonicalA = getCanonicalName(locA);
        String canonicalB = getCanonicalName(locB);

        if (canonicalA == null) {
            if (printMessage) System.out.println("[ERROR] Origin spot '" + locA + "' does not exist.");
            return false;
        }
        if (canonicalB == null) {
            if (printMessage) System.out.println("[ERROR] Destination spot '" + locB + "' does not exist.");
            return false;
        }
        if (canonicalA.equalsIgnoreCase(canonicalB)) {
            if (printMessage) System.out.println("[ERROR] Cannot connect a scenic spot to itself.");
            return false;
        }

        List<Edge> edgesA = adjacencyList.get(canonicalA);
        for (Edge e : edgesA) {
            if (e.getDestination().equalsIgnoreCase(canonicalB)) {
                if (printMessage) System.out.println("[ERROR] Route between '" + canonicalA + "' and '" + canonicalB + "' already exists.");
                return false;
            }
        }

        edgesA.add(new Edge(canonicalB, routeType));
        adjacencyList.get(canonicalB).add(new Edge(canonicalA, routeType));

        if (printMessage) {
            System.out.println("[SUCCESS] Route (" + routeType + ") between '" + canonicalA + "' and '" + canonicalB + "' added.");
        }
        return true;
    }

    // 4. Remove Route (Edge)
    public boolean removeRoute(String locA, String locB) {
        String canonicalA = getCanonicalName(locA);
        String canonicalB = getCanonicalName(locB);

        if (canonicalA == null || canonicalB == null) {
            System.out.println("[ERROR] One or both specified scenic spots do not exist.");
            return false;
        }

        List<Edge> edgesA = adjacencyList.get(canonicalA);
        List<Edge> edgesB = adjacencyList.get(canonicalB);

        boolean removedA = edgesA.removeIf(e -> e.getDestination().equalsIgnoreCase(canonicalB));
        boolean removedB = edgesB.removeIf(e -> e.getDestination().equalsIgnoreCase(canonicalA));

        if (removedA && removedB) {
            System.out.println("[SUCCESS] Route between '" + canonicalA + "' and '" + canonicalB + "' removed successfully.");
            return true;
        } else {
            System.out.println("[ERROR] No direct route exists between '" + canonicalA + "' and '" + canonicalB + "'.");
            return false;
        }
    }

    // 5. Search Scenic Spot
    public void searchScenicSpot(String spotName) {
        String canonical = getCanonicalName(spotName);
        if (canonical == null) {
            System.out.println("[ERROR] Scenic spot '" + spotName + "' not found in the network.");
            return;
        }

        List<Edge> connections = adjacencyList.get(canonical);

        System.out.println("\n=======================================================");
        System.out.println("                 SCENIC SPOT DETAILS                   ");
        System.out.println("=======================================================");
        System.out.printf(" Spot Name           : %s\n", canonical);
        System.out.printf(" Direct Connections  : %d route(s)\n", connections.size());
        System.out.println("-------------------------------------------------------");

        if (connections.isEmpty()) {
            System.out.println(" (No connected routes found for this spot)");
        } else {
            System.out.printf(" %-28s | %-20s\n", "Connected Location", "Route Type");
            System.out.println("------------------------------+------------------------");
            for (Edge edge : connections) {
                System.out.printf(" %-28s | %-20s\n", edge.getDestination(), edge.getRouteType());
            }
        }
        System.out.println("=======================================================\n");
    }

    // 6. Display Adjacency List Structure
    public void displayNetwork() {
        if (adjacencyList.isEmpty()) {
            System.out.println("\n[INFO] Network is currently empty. Please create or populate the graph.");
            return;
        }

        System.out.println("\n=========================================================================================");
        System.out.println("                         LANGKAWI SCENIC NETWORK (ADJACENCY LIST)                        ");
        System.out.println("=========================================================================================");
        System.out.printf(" %-22s | %-55s\n", "Scenic Spot (Vertex)", "Adjacent Connections [Destination (Type)]");
        System.out.println("-----------------------+-----------------------------------------------------------------");

        for (Map.Entry<String, List<Edge>> entry : adjacencyList.entrySet()) {
            String spot = entry.getKey();
            List<Edge> edges = entry.getValue();

            StringBuilder sb = new StringBuilder();
            if (edges.isEmpty()) {
                sb.append("[No Connections]");
            } else {
                for (int i = 0; i < edges.size(); i++) {
                    Edge e = edges.get(i);
                    sb.append(e.getDestination()).append(" (").append(e.getRouteType()).append(")");
                    if (i < edges.size() - 1) {
                        sb.append(", ");
                    }
                }
            }

            String connStr = sb.toString();
            if (connStr.length() > 55) {
                System.out.printf(" %-22s | %-55s\n", spot, connStr.substring(0, 55));
                System.out.printf(" %-22s | %-55s\n", "", connStr.substring(55));
            } else {
                System.out.printf(" %-22s | %-55s\n", spot, connStr);
            }
        }
        System.out.println("=========================================================================================\n");
    }

    // 7. Breadth-First Search (BFS) Traversal
    public void breadthFirstSearch(String startSpot) {
        String canonical = getCanonicalName(startSpot);
        if (canonical == null) {
            System.out.println("[ERROR] Starting spot '" + startSpot + "' does not exist in the network.");
            return;
        }

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        List<String> traversalOrder = new ArrayList<>();

        visited.add(canonical.toLowerCase());
        queue.add(canonical);

        System.out.println("\n=======================================================");
        System.out.println("       BREADTH-FIRST SEARCH (BFS) TRAVERSAL LOG        ");
        System.out.println("=======================================================");
        System.out.printf(" Starting Location: %s\n", canonical);
        System.out.println("-------------------------------------------------------");

        int step = 1;
        while (!queue.isEmpty()) {
            String current = queue.poll();
            traversalOrder.add(current);
            System.out.printf(" Step %-2d : Processing '%s'\n", step++, current);

            List<Edge> neighbors = adjacencyList.get(current);
            for (Edge edge : neighbors) {
                String neighbor = edge.getDestination();
                if (!visited.contains(neighbor.toLowerCase())) {
                    visited.add(neighbor.toLowerCase());
                    queue.add(neighbor);
                    System.out.printf("         --> Discovered & Queued: '%s'\n", neighbor);
                }
            }
        }

        System.out.println("-------------------------------------------------------");
        System.out.println(" Final Traversal Order:");
        System.out.println(" " + String.join(" -> ", traversalOrder));
        System.out.println("=======================================================\n");
    }

    private String formatName(String str) {
        String[] words = str.split("\\s+");
        StringBuilder formatted = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                formatted.append(Character.toUpperCase(word.charAt(0)))
                         .append(word.substring(1).toLowerCase())
                         .append(" ");
            }
        }
        return formatted.toString().trim();
    }
}

// Main Executable Public Class
public class LangkawiGraphSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ScenicGraph graph = new ScenicGraph();

    public static void main(String[] args) {
        graph.createDefaultNetwork();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readIntInput("Enter your selection (0-8): ", 0, 8);

            switch (choice) {
                case 1:
                    handleCreateGraph();
                    break;
                case 2:
                    handleAddScenicSpot();
                    break;
                case 3:
                    handleRemoveScenicSpot();
                    break;
                case 4:
                    handleAddRoute();
                    break;
                case 5:
                    handleRemoveRoute();
                    break;
                case 6:
                    handleSearchScenicSpot();
                    break;
                case 7:
                    graph.displayNetwork();
                    break;
                case 8:
                    handleBFSTraversal();
                    break;
                case 0:
                    running = handleExit();
                    break;
                default:
                    System.out.println("[ERROR] Invalid choice. Please try again.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("=======================================================");
        System.out.println("   LANGKAWI SKYCAB & HIKING TRAIL NETWORK SYSTEM       ");
        System.out.println("=======================================================");
        System.out.println("  1. Create / Reset Graph (Default Network)");
        System.out.println("  2. Add Scenic Spot");
        System.out.println("  3. Remove Scenic Spot");
        System.out.println("  4. Add Route (Cable Car / Walking / Hiking)");
        System.out.println("  5. Remove Route");
        System.out.println("  6. Search Scenic Spot");
        System.out.println("  7. Display Network (Adjacency List)");
        System.out.println("  8. Breadth-First Search (BFS) Traversal");
        System.out.println("  0. Exit System");
        System.out.println("=======================================================");
    }

    private static void handleCreateGraph() {
        if (!graph.isEmpty()) {
            String confirm = readStringInput("Existing graph data will be reset. Proceed? (Y/N): ");
            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("[INFO] Reset cancelled.");
                return;
            }
        }
        graph.createDefaultNetwork();
    }

    private static void handleAddScenicSpot() {
        System.out.println("\n--- Add New Scenic Spot ---");
        String name = readStringInput("Enter scenic spot name: ");
        graph.addScenicSpot(name, true);
    }

    private static void handleRemoveScenicSpot() {
        System.out.println("\n--- Remove Scenic Spot ---");
        if (graph.isEmpty()) {
            System.out.println("[ERROR] Graph is empty. No scenic spots to remove.");
            return;
        }
        String name = readExistingSpotInput("Enter scenic spot name to remove: ");
        graph.removeScenicSpot(name);
    }

    private static void handleAddRoute() {
        System.out.println("\n--- Add New Route ---");
        if (graph.getSpotCount() < 2) {
            System.out.println("[ERROR] Network must have at least 2 scenic spots to create a route.");
            return;
        }
        String locA = readExistingSpotInput("Enter 1st scenic spot: ");
        String locB = readExistingSpotInput("Enter 2nd scenic spot: ");

        System.out.println("Select Route Type:");
        System.out.println("  1. Cable Car Route");
        System.out.println("  2. Walking Path");
        System.out.println("  3. Hiking Trail");
        int typeChoice = readIntInput("Selection (1-3): ", 1, 3);

        String routeType;
        switch (typeChoice) {
            case 1: routeType = "Cable Car Route"; break;
            case 2: routeType = "Walking Path"; break;
            case 3: routeType = "Hiking Trail"; break;
            default: routeType = "Walking Path";
        }

        graph.addRoute(locA, locB, routeType, true);
    }

    private static void handleRemoveRoute() {
        System.out.println("\n--- Remove Route ---");
        if (graph.getSpotCount() < 2) {
            System.out.println("[ERROR] Network must have at least 2 scenic spots to remove a route.");
            return;
        }
        String locA = readExistingSpotInput("Enter 1st scenic spot: ");
        String locB = readExistingSpotInput("Enter 2nd scenic spot: ");
        graph.removeRoute(locA, locB);
    }

    private static void handleSearchScenicSpot() {
        System.out.println("\n--- Search Scenic Spot ---");
        if (graph.isEmpty()) {
            System.out.println("[ERROR] Graph is empty. Cannot search.");
            return;
        }
        String name = readExistingSpotInput("Enter scenic spot name to search: ");
        graph.searchScenicSpot(name);
    }

    private static void handleBFSTraversal() {
        System.out.println("\n--- Breadth-First Search (BFS) Traversal ---");
        if (graph.isEmpty()) {
            System.out.println("[ERROR] Graph is empty. Cannot perform BFS.");
            return;
        }
        String start = readExistingSpotInput("Enter starting scenic spot for BFS: ");
        graph.breadthFirstSearch(start);
    }

    private static boolean handleExit() {
        String confirm = readStringInput("Are you sure you want to exit? (Y/N): ");
        if (confirm.equalsIgnoreCase("Y")) {
            System.out.println("\nThank you for using the Langkawi SkyCab Network System. Goodbye!");
            return false;
        }
        return true;
    }

    // Repeatedly prompts until the user enters a spot that exists in the network
    private static String readExistingSpotInput(String prompt) {
        while (true) {
            String input = readStringInput(prompt);
            String canonical = graph.getCanonicalName(input);
            if (canonical != null) {
                return canonical;
            }
            System.out.println("[ERROR] Scenic spot does not exist in the network. Please enter an existing spot.");
        }
    }

    // Input sanitizer preventing InputMismatchException on letters or symbols
    private static int readIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val >= min && val <= max) {
                    return val;
                } else {
                    System.out.printf("[ERROR] Input out of range. Please enter a number between %d and %d.\n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid entry. Please enter numbers only.");
            }
        }
    }

    // Input handler checking for empty string values
    private static String readStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("[ERROR] Input cannot be blank. Please enter valid text.");
        }
    }
}