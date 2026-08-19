import java.util.*;

// Class representing a directed/undirected edge in the graph
class Edge {
    private String destination;
    private String routeType;

    public Edge(String destination, String routeType) {
        this.destination = destination;
        this.routeType = routeType;
    }

    public String getDestination() { return destination; }
    public String getRouteType() { return routeType; }
}

// Core Adjacency List Graph Implementation
class ScenicGraph {
    // Map storing Spot Name -> List of Connected Edges
    private Map<String, List<Edge>> adjacencyList = new LinkedHashMap<>();

    // 1. Initialize Default Langkawi SkyCab Network (10 Spots, 11 Routes)
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

        System.out.println("\n[SUCCESS] Default network created (10 spots, 11 routes).");
    }

    // Helper: Case-insensitive lookup for vertex names
    public String getCanonicalName(String name) {
        if (name == null) return null;
        for (String key : adjacencyList.keySet()) {
            if (key.equalsIgnoreCase(name.trim())) return key;
        }
        return null;
    }

    public boolean isEmpty() { return adjacencyList.isEmpty(); }
    public Set<String> getSpotNames() { return adjacencyList.keySet(); }

    // 2. Add Scenic Spot (Vertex)
    public boolean addScenicSpot(String spotName, boolean printMsg) {
        if (spotName == null || spotName.trim().isEmpty()) {
            if (printMsg) System.out.println("[ERROR] Spot name cannot be empty.");
            return false;
        }
        if (getCanonicalName(spotName) != null) {
            if (printMsg) System.out.println("[ERROR] Spot already exists.");
            return false;
        }
        adjacencyList.put(spotName.trim(), new ArrayList<>());
        if (printMsg) System.out.println("[SUCCESS] Added spot: " + spotName.trim());
        return true;
    }

    // 3. Remove Scenic Spot (Vertex)
    public boolean removeScenicSpot(String spotName) {
        String canonical = getCanonicalName(spotName);
        if (canonical == null) {
            System.out.println("[ERROR] Scenic spot not found.");
            return false;
        }
        adjacencyList.remove(canonical);
        // Remove all edges referencing this spot
        for (List<Edge> edges : adjacencyList.values()) {
            edges.removeIf(e -> e.getDestination().equalsIgnoreCase(canonical));
        }
        System.out.println("[SUCCESS] Removed spot: " + canonical);
        return true;
    }

    // 4. Add Route (Edge) - Undirected
    public boolean addRoute(String locA, String locB, String type, boolean printMsg) {
        String canonicalA = getCanonicalName(locA);
        String canonicalB = getCanonicalName(locB);

        if (canonicalA == null || canonicalB == null) {
            if (printMsg) System.out.println("[ERROR] One or both scenic spots do not exist.");
            return false;
        }
        if (canonicalA.equalsIgnoreCase(canonicalB)) {
            if (printMsg) System.out.println("[ERROR] Cannot connect a spot to itself.");
            return false;
        }
        // Check for existing route
        for (Edge e : adjacencyList.get(canonicalA)) {
            if (e.getDestination().equalsIgnoreCase(canonicalB)) {
                if (printMsg) System.out.println("[ERROR] Route already exists between these spots.");
                return false;
            }
        }

        adjacencyList.get(canonicalA).add(new Edge(canonicalB, type));
        adjacencyList.get(canonicalB).add(new Edge(canonicalA, type));
        if (printMsg) System.out.println("[SUCCESS] Route added between " + canonicalA + " and " + canonicalB);
        return true;
    }

    // 5. Remove Route (Edge)
    public boolean removeRoute(String locA, String locB) {
        String canonicalA = getCanonicalName(locA);
        String canonicalB = getCanonicalName(locB);

        if (canonicalA == null || canonicalB == null) {
            System.out.println("[ERROR] One or both scenic spots do not exist.");
            return false;
        }

        boolean removedA = adjacencyList.get(canonicalA).removeIf(e -> e.getDestination().equalsIgnoreCase(canonicalB));
        boolean removedB = adjacencyList.get(canonicalB).removeIf(e -> e.getDestination().equalsIgnoreCase(canonicalA));

        if (removedA && removedB) {
            System.out.println("[SUCCESS] Route removed between " + canonicalA + " and " + canonicalB);
            return true;
        }
        System.out.println("[ERROR] No direct route exists between these spots.");
        return false;
    }

    // 6. Search Scenic Spot
    public void searchScenicSpot(String spotName) {
        String canonical = getCanonicalName(spotName);
        if (canonical == null) {
            System.out.println("[ERROR] Scenic spot not found.");
            return;
        }

        List<Edge> connections = adjacencyList.get(canonical);
        System.out.println("\n--- SPOT DETAILS: " + canonical + " ---");
        System.out.println("Total Routes: " + connections.size());
        if (connections.isEmpty()) {
            System.out.println("No outgoing or incoming routes.");
        } else {
            for (Edge edge : connections) {
                System.out.printf(" -> Destination: %-22s | Route Type: %s\n", edge.getDestination(), edge.getRouteType());
            }
        }
        System.out.println();
    }

    // 7. Display Network (Adjacency List)
    public void displayNetwork() {
        if (adjacencyList.isEmpty()) {
            System.out.println("\n[INFO] Graph is currently empty.");
            return;
        }
        System.out.println("\n=======================================================");
        System.out.println("        LANGKAWI SCENIC NETWORK (ADJACENCY LIST)       ");
        System.out.println("=======================================================");
        for (Map.Entry<String, List<Edge>> entry : adjacencyList.entrySet()) {
            System.out.println(" Spot Vertex: " + entry.getKey());
            if (entry.getValue().isEmpty()) {
                System.out.println("   [No Connected Routes]");
            } else {
                for (Edge edge : entry.getValue()) {
                    System.out.printf("   --> Connected to: %-22s | Type: %s\n", edge.getDestination(), edge.getRouteType());
                }
            }
            System.out.println("-------------------------------------------------------");
        }
    }

    // 8. Breadth-First Search Traversal (BFS)
    public void breadthFirstSearch(String startSpot) {
        String canonical = getCanonicalName(startSpot);
        if (canonical == null) {
            System.out.println("[ERROR] Starting spot does not exist.");
            return;
        }

        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        List<String> order = new ArrayList<>();

        visited.add(canonical.toLowerCase());
        queue.add(canonical);

        System.out.println("\n--- BREADTH-FIRST SEARCH (BFS) TRAVERSAL ---");
        System.out.println("Start Node: " + canonical);

        int step = 1;
        while (!queue.isEmpty()) {
            String current = queue.poll();
            order.add(current);
            System.out.printf(" Step %d: Visited '%s'\n", step++, current);

            for (Edge edge : adjacencyList.get(current)) {
                String neighbor = edge.getDestination();
                if (!visited.contains(neighbor.toLowerCase())) {
                    visited.add(neighbor.toLowerCase());
                    queue.add(neighbor);
                    System.out.println("   --> Queued neighbor: " + neighbor);
                }
            }
        }
        
        System.out.print("\nFinal BFS Path: ");
        for (int i = 0; i < order.size(); i++) {
            System.out.print(order.get(i));
            if (i < order.size() - 1) {
                System.out.print(" to ");
            }
        }
        System.out.println("\n");
    }
}

// Main Interactive Driver Application
public class LangkawiGraphSystem {
    private static final Scanner sc = new Scanner(System.in);
    private static final ScenicGraph graph = new ScenicGraph();

    public static void main(String[] args) {
        graph.createDefaultNetwork();

        while (true) {
            printMenu();
            int choice = readInt("Select an option (0-8): ", 0, 8);

            switch (choice) {
                case 1:
                    graph.createDefaultNetwork();
                    break;
                case 2:
                    System.out.print("Enter new spot name: ");
                    graph.addScenicSpot(sc.nextLine(), true);
                    break;
                case 3:
                    printAvailableSpots();
                    System.out.print("Enter spot name to remove: ");
                    graph.removeScenicSpot(sc.nextLine());
                    break;
                case 4:
                    printAvailableSpots();
                    System.out.print("Enter 1st spot: ");
                    String a = sc.nextLine();
                    System.out.print("Enter 2nd spot: ");
                    String b = sc.nextLine();
                    System.out.println("Route Types: 1. Cable Car Route | 2. Walking Path | 3. Hiking Trail");
                    int typeChoice = readInt("Select Route Type (1-3): ", 1, 3);
                    String type;
                    switch (typeChoice) {
                        case 1:
                            type = "Cable Car Route";
                            break;
                        case 2:
                            type = "Walking Path";
                            break;
                        default:
                            type = "Hiking Trail";
                            break;
                    }
                    graph.addRoute(a, b, type, true);
                    break;
                case 5:
                    printAvailableSpots();
                    System.out.print("Enter 1st spot: ");
                    String spotA = sc.nextLine();
                    System.out.print("Enter 2nd spot: ");
                    String spotB = sc.nextLine();
                    graph.removeRoute(spotA, spotB);
                    break;
                case 6:
                    printAvailableSpots();
                    System.out.print("Enter spot name to search: ");
                    graph.searchScenicSpot(sc.nextLine());
                    break;
                case 7:
                    graph.displayNetwork();
                    break;
                case 8:
                    printAvailableSpots();
                    System.out.print("Enter start spot for BFS: ");
                    graph.breadthFirstSearch(sc.nextLine());
                    break;
                case 0:
                    System.out.println("Exiting System. Goodbye!");
                    return;
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=======================================================");
        System.out.println("    LANGKAWI SKYCAB & HIKING TRAIL NETWORK SYSTEM");
        System.out.println("=======================================================");
        System.out.println(" 1. Create/Reset Graph (Default 10 Spots, 11 Routes)");
        System.out.println(" 2. Add Scenic Spot");
        System.out.println(" 3. Remove Scenic Spot");
        System.out.println(" 4. Add Route");
        System.out.println(" 5. Remove Route");
        System.out.println(" 6. Search Scenic Spot");
        System.out.println(" 7. Display Network (Adjacency List)");
        System.out.println(" 8. Breadth-First Search (BFS)");
        System.out.println(" 0. Exit");
        System.out.println("=======================================================");
    }

    private static void printAvailableSpots() {
        if (graph.isEmpty()) return;
        System.out.println("\nCurrent Network Spots: " + graph.getSpotNames());
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
            } catch (NumberFormatException ignored) {}
            System.out.printf("[ERROR] Please enter a valid number between %d and %d.\n", min, max);
        }
    }
}