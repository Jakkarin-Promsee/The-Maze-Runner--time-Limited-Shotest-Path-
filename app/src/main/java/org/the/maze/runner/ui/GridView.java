package org.the.maze.runner.ui;

import java.util.List;
import org.the.maze.runner.algorithm.*;
import org.the.maze.runner.model.*;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class GridView {

    // Maze Grid data store
    private Grid grid;

    // Maze Screen Size
    private int maxWidth;
    private int maxHeight;

    // Initialail Class
    public GridView(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    // Set Screen size
    public void setScreen(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public Pane draw(String gridText) {
        // Initilize Pane
        Pane gridPane = new Pane();

        // Parse the string and initialize the Grid model
        grid = parseWeightedMaze(gridText);

        // Draw the initial grid no path setup (walls, start, end, weights)
        drawGridVisualization(gridPane, null);

        return gridPane;
    }

    public Pane drawPath(PathFindingAlgorithm algorithm) {
        // Initilize Pane
        Pane gridPane = new Pane();

        // If it has no maze grid and algorith, path==null
        if (algorithm == null || grid == null)
            return gridPane;

        // Get start/end nodes from the model
        Node start = grid.getStartNode();
        Node end = grid.getEndNode();

        // Prevent maze solve conflict
        if (start == null || end == null) {
            System.err.println("Start or End node not found in the grid.");
            return gridPane;
        }

        // Find the path
        List<Node> path = algorithm.findPath(grid, start, end);

        // Re-draw the entire grid, passing the found path to highlight it.
        drawGridVisualization(gridPane, path);

        return gridPane;
    }

    private int calculateTileSize() {
        return Math.min(maxWidth / grid.getHeight(), maxHeight / grid.getWidth());
    }

    // Draw grid (walls, start, end, weights || path)
    private void drawGridVisualization(Pane gridPane, List<Node> path) {
        // Ensure the pane exists before clearing/adding children
        if (gridPane == null)
            return;
        gridPane.getChildren().clear();

        // Get tile size to make full maze in screen max size
        int tileSize = calculateTileSize();

        // Build Grid Maze
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Node n = grid.getNode(x, y);

                // 1. Create the base Rectangle (The Cell)
                Rectangle rect = new Rectangle(tileSize, tileSize);
                rect.setX(n.x * tileSize);
                rect.setY(n.y * tileSize);

                // Determine base color based on node type
                Color baseColor;
                if (n.isVoid()) {
                    baseColor = Color.GRAY; // Wall color
                } else if (n.isWall()) {
                    baseColor = Color.BLACK; // Wall color
                } else if (n.isStart()) {
                    baseColor = Color.GREEN; // Start color
                } else if (n.isEnd()) {
                    baseColor = Color.RED; // End color
                } else {
                    // White/light gray for weighted nodes
                    baseColor = Color.web("#f0f0f0");
                }
                rect.setFill(baseColor);

                // Dark border for cell separation
                rect.setStroke(Color.web("#333333"));

                // If a path exists and this node is on the path, override color
                if (path != null && path.contains(n) && !n.isStart() && !n.isEnd()) {
                    rect.setFill(Color.YELLOW); // Path color
                }

                // Add cell to the Pane
                gridPane.getChildren().add(rect);

                // 2. Add Weight Label if it's a weighted node and not a wall
                if (n.getWeight() >= 1 && !n.isStart() && !n.isEnd()) {
                    Label weightLabel = new Label(String.valueOf(n.getWeight()));

                    // Dynamic font size based on TILE_SIZE
                    weightLabel.setStyle("-fx-font-size:" + ((int) tileSize / 3)
                            + "px; -fx-text-fill: black; -fx-font-weight: bold;");

                    // Position the label in the center of the cell
                    // Note: Centering labels is complex due to font metrics.
                    // These offsets are approximations (x-5, y-8) from the previous code.
                    weightLabel.setLayoutX(n.x * tileSize + tileSize / 2 - (weightLabel.getText().length() * 3));
                    weightLabel.setLayoutY(n.y * tileSize + tileSize / 2 - 8);

                    // Add label to the Pane
                    gridPane.getChildren().add(weightLabel);
                }
            }
        }

        // Final re-draw of start/end to ensure they are on top of the path color
        drawStartEndNodes(gridPane, path);
    }

    // draw start/end on top
    private void drawStartEndNodes(Pane gridPane, List<Node> path) {
        // Call start and end node
        Node start = grid.getStartNode();
        Node end = grid.getEndNode();

        // Get tile size to make full maze in screen max size
        int tileSize = calculateTileSize();

        if (start != null) {
            Rectangle startRect = createSpecialRect(start, Color.GREEN, tileSize);
            gridPane.getChildren().add(startRect);
        }

        if (end != null) {
            Color endColor = (path != null && path.contains(end)) ? Color.RED.darker() : Color.RED;
            Rectangle endRect = createSpecialRect(end, endColor, tileSize);
            gridPane.getChildren().add(endRect);
        }
    }

    // Rectangle node
    private Rectangle createSpecialRect(Node n, Color color, int size) {
        Rectangle rect = new Rectangle(size, size);
        rect.setX(n.x * size);
        rect.setY(n.y * size);
        rect.setFill(color);
        rect.setStroke(Color.web("#CCCCCC")); // Light border
        return rect;
    }

    // Input Parsing from Sting to Grid
    private Grid parseWeightedMaze(String input) {
        String[] rows = input.trim().split("\n");
        int height = rows.length;

        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("#|S|G|\"\\d+\"|\n").matcher(input);

        // Calculate width by summing tokens in the first meaningful row
        int width = 0;

        if (rows.length > 0) {
            // Use a loop to find the width (max column count)
            for (String row : rows) { // Iterate over all rows
                java.util.regex.Matcher widthMatcher = java.util.regex.Pattern.compile("#|S|G|\"\\d+\"")
                        .matcher(row);

                int currentWidth = 1; // count \n too
                while (widthMatcher.find()) {
                    currentWidth++;
                }
                // Update width to be the maximum width found so far
                if (currentWidth > width) {
                    width = currentWidth;
                }
            }

        }

        Grid newGrid = new Grid(width - 1, height);

        int x = 0;
        int y = 0;

        matcher.reset();
        while (matcher.find()) {
            String token = matcher.group();

            // Row transition logic
            if (x >= (width)) {
                x = 0;
                y++;
            }
            if (y >= height)
                break;

            if (token.equals("\n")) {
                while (x < (width - 1)) {
                    Node node = newGrid.getNode(x, y);
                    node.setVoid(true);
                    x++;
                }

                x = 0;
                y++;
                continue;
            }

            Node node = newGrid.getNode(x, y);
            if (token.equals("#")) {
                node.setWall(true);
                node.setWeight(0);
            } else if (token.equals("S")) {
                node.setStart(true);
                node.setWeight(1);
                newGrid.setStartNode(node);
            } else if (token.equals("G")) {
                node.setEnd(true);
                node.setWeight(1);
                newGrid.setEndNode(node);
            } else if (token.startsWith("\"") && token.endsWith("\"")) {
                try {
                    int weight = Integer.parseInt(token.substring(1, token.length() - 1));
                    node.setWeight(weight);
                    node.setVoid(false);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid weight token: " + token);
                    node.setWeight(1);
                }
            } else {
                node.setWeight(1);
            }

            x++;
        }

        System.out.println("Maze parsed successfully: " + width + "x" + height);
        return newGrid;
    }
}