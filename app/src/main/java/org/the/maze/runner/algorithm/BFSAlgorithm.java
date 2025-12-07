package org.the.maze.runner.algorithm;

import org.the.maze.runner.model.Grid;
import org.the.maze.runner.model.Node;

import java.util.*;

public class BFSAlgorithm implements PathFindingAlgorithm {

    @Override
    public List<Node> findPath(Grid grid, Node start, Node end) {
        // 1. Queue for nodes to visit
        Queue<Node> queue = new LinkedList<>();
        queue.add(start);

        // 2. Map to track the parent/predecessor of each node
        // This is necessary to reconstruct the final path from end to start.
        Map<Node, Node> parentMap = new HashMap<>();

        // 3. Set to track visited nodes to avoid cycles and redundant processing
        Set<Node> visited = new HashSet<>();
        visited.add(start);

        // --- Core BFS Loop ---
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Check if we reached the goal
            if (current.equals(end)) {
                return AlgorithmUtils.reconstructPath(parentMap, start, end);
            }

            // Iterate over valid neighbors (non-wall, non-void)
            // Note: The Grid.getNeighbors() already handles wall check.
            for (Node neighbor : grid.getNeighbors(current)) {

                // We also need to ensure the node isn't marked as void,
                // though usually non-wall nodes are paths/weighted.
                if (!visited.contains(neighbor) && !neighbor.isVoid()) {
                    // Mark as visited
                    visited.add(neighbor);

                    // Record the parent/predecessor
                    parentMap.put(neighbor, current);

                    // Add to the queue for next iteration
                    queue.add(neighbor);
                }
            }
        }

        // If the queue is empty and the 'end' node was not reached
        return Collections.emptyList();
    }
}
