package asgm;

/**
 * Any vertex that wants to be drawn by GraphView must implement this,
 * as taught in Practical 9B (Graph Visualization section).
 */
public interface Displayable {
    public int getX(); // Get x-coordinate of the vertex
    public int getY(); // Get y-coordinate of the vertex
    public String getName(); // Get display name of the vertex
}
