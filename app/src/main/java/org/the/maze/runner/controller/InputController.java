package org.the.maze.runner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.the.maze.runner.App;
import org.the.maze.runner.maze_generate.Maze;
import org.the.maze.runner.maze_generate.MazeGenerator;
import org.the.maze.runner.maze_generate.MazePrinter;
import org.the.maze.runner.ui.GridView;

public class InputController {

    // Maze data input text area
    @FXML
    private TextArea mazeInputArea;

    // Maze Generate input text area
    @FXML
    private TextArea mazeInputSeed;
    @FXML
    private TextArea mazeGenerateWidth;
    @FXML
    private TextArea mazeGenerateHeight;

    // Maze Visualize, Using javafx Pane to store 2D tiles
    @FXML
    private Pane gridPane;

    // The class to build javafx pane
    private GridView gridView;

    // Define tile size for visualization
    private static final int maxWidth = 300;
    private static final int maxHeight = 300;

    // Intialize when load scene
    @FXML
    public void initialize() {
        // Intialize grid view class size
        this.gridView = new GridView(maxWidth, maxHeight);

        // Listen for text changes in the input TextArea
        mazeInputArea.textProperty().addListener((observable, oldValue, newValue) -> {
            // Build and Update gride Pane
            Pane initialVisualization = gridView.draw(newValue);
            updateVisualizationPane(initialVisualization);
        });
    }

    // Set screen size
    private void updateVisualizationPaneScreen(int weidth, int height) {
        gridView.setScreen(weidth, height);
    }

    // Update Pane with new Pane
    private void updateVisualizationPane(Pane newPane) {
        if (gridPane == null) {
            System.err.println("Error: FXML gridPane container is null.");
            return;
        }

        // 1. Clear all existing children from the container
        gridPane.getChildren().clear();

        // 2. Add the new Pane containing the visualization
        gridPane.getChildren().add(newPane);
    }

    // Called when user clicks "Choose Maze File"
    @FXML
    private void onChooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Maze File");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        Stage stage = App.getPrimaryStage();
        File file = chooser.showOpenDialog(stage);

        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                loadInputText(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Load Example buttons function
    @FXML
    private void onExample1() {
        loadFileFromPath("m15_15.txt");
    }

    @FXML
    private void onExample2() {
        loadFileFromPath("m40_40.txt");
    }

    @FXML
    private void onExample3() {
        loadFileFromPath("m50_50.txt");
    }

    @FXML
    private void onExample4() {
        loadFileFromPath("m60_60.txt");
    }

    @FXML
    private void onExample5() {
        loadFileFromPath("m70_60.txt");
    }

    @FXML
    private void onExample6() {
        loadFileFromPath("m100_100.txt");
    }

    // Load maze from File Class
    private void loadMazeFromFile(File file) {
        try {
            String content = Files.readString(file.toPath());
            loadInputText(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lad maze from String Class
    private void loadFileFromPath(String name) {
        try {
            URL url = getClass().getResource("/org/the/maze/runner/maze_example/" + name);

            if (url == null) {
                System.out.println("File not found: " + url);
                return;
            }

            String content = Files.readString(Path.of(url.toURI()));
            loadInputText(content);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load new text to input areas
    private void loadInputText(String content) {
        mazeInputArea.setText(content);
    }

    // Load maze by generate
    @FXML
    private void onGenerateMaze() {
        System.out.println(
                Integer.parseInt(mazeGenerateWidth.getText()) + "/" +
                        Integer.parseInt(mazeGenerateHeight.getText()));
        Maze maze = MazeGenerator.generate(
                Integer.parseInt(mazeGenerateWidth.getText()),
                Integer.parseInt(mazeGenerateHeight.getText()));

        loadInputText(MazePrinter.toString(maze));
    }

    // Go back button to main page
    @FXML
    private void onBack() {
        App.setRoot("main-view");
    }

    // Go continue to solve page
    @FXML
    private void onContinue() {
        String mazeText = mazeInputArea.getText().trim();

        if (mazeText.isEmpty()) {
            System.out.println("Maze is empty!");
            return;
        }

        // Pass data to another controller
        App.setMaze(mazeText);

        // Load next page
        App.setRoot("maze-solve");
    }
}