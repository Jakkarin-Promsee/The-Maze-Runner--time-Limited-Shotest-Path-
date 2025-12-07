package org.the.maze.runner.maze_generate;

import java.io.FileWriter;
import java.io.IOException;

public class MazeFileWriter {

    public static void saveToFile(Maze maze, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {

            writer.write(MazePrinter.toString(maze));

            System.out.println("Maze saved to " + filename);

        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}
