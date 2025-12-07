package org.the.maze.runner.model;

import java.util.List;

public class Grid {

    private final int width;
    private final int height;
    private final Node[][] nodes; // Stores all nodes in the grid
    private Node startNode;
    private Node endNode;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.nodes = new Node[width][height];

        // Initialize all nodes in the grid
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.nodes[x][y] = new Node(x, y);
            }
        }
    }

    public Node getNode(int x, int y) {
        // Basic boundary check
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return nodes[x][y];
        }
        return null; // Return null if coordinates are out of bounds
    }

    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new java.util.ArrayList<>();
        int x = node.x;
        int y = node.y;

        // Check 4 directions: Right, Left, Down, Up
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            Node neighbor = getNode(newX, newY);

            // Add the neighbor if it exists and is not a wall
            if (neighbor != null && !neighbor.isWall()) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }
}