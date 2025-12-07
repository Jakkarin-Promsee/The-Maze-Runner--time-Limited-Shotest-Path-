package org.the.maze.runner.model;

public class Node {

    // --- Core Properties (Immutable Coordinates) ---
    public final int x; // Column index
    public final int y; // Row index

    // --- Maze/Grid State Properties ---
    private int weight = 0; // Cost to traverse this node (default is 0)
    private boolean isVoid = true;
    private boolean isWall = false; // True if it's a blocked cell (#)
    private boolean isStart = false; // True if it's the starting point (S)
    private boolean isEnd = false; // True if it's the destination (G)

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.isVoid = false;
        this.weight = weight;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean isWall) {
        this.isVoid = !isWall;
        this.isWall = isWall;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public void setVoid(boolean isVoid) {
        this.isVoid = isVoid;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean isStart) {
        this.isVoid = !isStart;
        this.isStart = isStart;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean isEnd) {
        this.isVoid = !isEnd;
        this.isEnd = isEnd;
    }

    // --- Utility Method for Debugging/Hashing ---
    @Override
    public String toString() {
        return "(" + x + ", " + y + ") [Weight: " + weight + "]";
    }

}