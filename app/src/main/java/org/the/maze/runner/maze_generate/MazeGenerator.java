package org.the.maze.runner.maze_generate;

import java.util.Random;

public class MazeGenerator {

    private static Maze maze;
    private static Random rand = new Random();

    public static Maze generate(int width, int height) {
        // FIX 1: Ensure dimensions are Odd numbers to guarantee a path to the goal
        if (height % 2 == 0)
            height++;
        if (width % 2 == 0)
            width++;

        maze = new Maze(width, height);
        carve(0, 0);
        assignWeights();

        // Ensure start & goal are paths
        maze.get(0, 0).setWall(false);
        maze.get(height - 1, width - 1).setWall(false);

        return maze;
    }

    private static void carve(int r, int c) {
        maze.get(r, c).setWall(false);

        int[] dirs = { 0, 1, 2, 3 };
        shuffle(dirs);

        for (int d : dirs) {
            int nr = r, nc = c;

            switch (d) {
                case 0:
                    nr = r - 2;
                    break; // up
                case 1:
                    nr = r + 2;
                    break; // down
                case 2:
                    nc = c - 2;
                    break; // left
                case 3:
                    nc = c + 2;
                    break; // right
            }

            if (isValid(nr, nc) && maze.get(nr, nc).isWall()) {
                // Break the wall between the two cells
                maze.get((r + nr) / 2, (c + nc) / 2).setWall(false);
                carve(nr, nc);
            }
        }
    }

    private static boolean isValid(int r, int c) {
        return (r >= 0 && c >= 0 && r < maze.getHeight() && c < maze.getWidth());
    }

    private static void shuffle(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
        }
    }

    private static void assignWeights() {
        for (int r = 0; r < maze.getHeight(); r++)
            for (int c = 0; c < maze.getWidth(); c++)
                if (!maze.get(r, c).isWall()) {
                    maze.get(r, c).setVoid(false);
                    maze.get(r, c).setWeight(rand.nextInt(10) + 1);
                }

    }
}