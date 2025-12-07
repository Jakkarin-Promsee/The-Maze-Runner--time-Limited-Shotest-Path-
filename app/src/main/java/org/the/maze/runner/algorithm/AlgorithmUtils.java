package org.the.maze.runner.algorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.the.maze.runner.model.Node;

public class AlgorithmUtils {
    /**
     * Helper function to backtrack from the end node to the start node
     * using the parentMap to build the final path list.
     */
    public static List<Node> reconstructPath(Map<Node, Node> parentMap, Node start, Node end) {
        List<Node> path = new LinkedList<>();
        Node current = end;

        // Backtrack from end to start using the recorded parents
        while (current != null) {
            path.add(0, current); // Add to the front to maintain correct order (start -> end)
            current = parentMap.get(current);

            // Safety break if we somehow cycle or reach the start node (which has no parent
            // in the map)
            if (current != null && current.equals(start)) {
                path.add(0, start);
                break;
            }
        }

        // If the path reconstruction started correctly but didn't find the start node,
        // the path will be incomplete. Since we ensure it breaks on finding the end
        // node,
        // this is mostly for completeness.
        return path;
    }
}
