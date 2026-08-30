package langkawigraphsystem;

public class ScenicSpot implements Displayable {
    private String name;
    private int x;
    private int y;

    public ScenicSpot(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
