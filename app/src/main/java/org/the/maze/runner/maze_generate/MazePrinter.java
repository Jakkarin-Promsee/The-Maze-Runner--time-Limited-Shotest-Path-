package org.the.maze.runner.maze_generate;

import org.the.maze.runner.model.Node;

public class MazePrinter {

    public static String toString(Maze maze) {
        StringBuilder out = new StringBuilder();

        int h = maze.getHeight();
        int w = maze.getWidth();

        // FIX: Add +2 to cover the left and right borders
        out.append("#".repeat(w + 2)).append("\n");

        for (int r = 0; r < h; r++) {
            out.append("#"); // left border

            for (int c = 0; c < w; c++) {
                Node cell = maze.get(r, c);

                if (cell.isWall()) {
                    out.append("#");
                    continue;
                }
                if (r == 0 && c == 0) {
                    out.append("S");
                    continue;
                }
                if (r == h - 1 && c == w - 1) {
                    out.append("G");
                    continue;
                }
                out.append("\"").append(cell.getWeight()).append("\"");
            }

            out.append("#").append("\n"); // right border
        }

        // FIX: Add +2 here as well
        out.append("#".repeat(w + 2)).append("\n");

        return out.toString();
    }
}