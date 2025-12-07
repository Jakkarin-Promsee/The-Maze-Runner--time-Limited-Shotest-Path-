package org.the.maze.runner.maze_generate;

import org.the.maze.runner.model.Grid;
import org.the.maze.runner.model.Node;

public class Maze {
    private int width;
    private int height;
    private Grid grid;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Grid(width, height);

        // Set wall for all
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid.getNode(i, j).setWall(true);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Grid getGrid() {
        return grid;
    }

    public Node get(int r, int c) {
        return grid.getNode(c, r);
    }
}
