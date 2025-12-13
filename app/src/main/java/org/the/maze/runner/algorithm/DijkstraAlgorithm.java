package org.the.maze.runner.algorithm;

import org.the.maze.runner.model.Grid;
import org.the.maze.runner.model.Node;

import java.util.*;

public class DijkstraAlgorithm implements PathFindingAlgorithm {

    @Override
    public List<Node> findPath(Grid grid, Node start, Node end) {
        // 1. PriorityQueue for nodes to visit
        // Using PriorityQueue instead of standard Queue to always process the lowest cost node first.
        PriorityQueue<NodeWrapper> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(nw -> nw.totalCost));
        priorityQueue.add(new NodeWrapper(start, 0));

        // 2. Map to track the shortest distance to each node found so far
        // We initialize the start node with distance 0.
        Map<Node, Integer> distanceMap = new HashMap<>();
        distanceMap.put(start, 0);

        // 3. Map to track the parent/predecessor of each node
        // This is necessary to reconstruct the final path from end to start.
        Map<Node, Node> parentMap = new HashMap<>();

        // 4. Set to track visited nodes to avoid processing the same node multiple times
        Set<Node> visited = new HashSet<>();

        // --- Core Dijkstra Loop ---
        while (!priorityQueue.isEmpty()) {
            // Poll the node with the lowest total cost
            NodeWrapper currentWrapper = priorityQueue.poll();
            Node current = currentWrapper.node;

            // Check if we reached the goal
            if (current.equals(end)) {
                return AlgorithmUtils.reconstructPath(parentMap, start, end);
            }

            // If we have already visited this node, skip it
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            // Iterate over valid neighbors (non-wall, non-void)
            // Note: The Grid.getNeighbors() already handles wall check.
            for (Node neighbor : grid.getNeighbors(current)) {
                
                // Ensure the node isn't marked as void
                if (!neighbor.isVoid()) {
                    // Calculate new cost:
                    // Current distance + Base movement cost (1) + Node specific weight
                    int newDist = distanceMap.get(current) + 1 + neighbor.getWeight();

                    // Relaxation: Check if we found a shorter path to this neighbor
                    if (newDist < distanceMap.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                        // Update distance and parent
                        distanceMap.put(neighbor, newDist);
                        parentMap.put(neighbor, current);

                        // Add to the priority queue for next processing
                        priorityQueue.add(new NodeWrapper(neighbor, newDist));
                    }
                }
            }
        }

        // If the queue is empty and the 'end' node was not reached
        return Collections.emptyList();
    }

    // --- Helper Class ---
    // Wrapper class to store Node and its total cost in the PriorityQueue
    private static class NodeWrapper {
        Node node;
        int totalCost;

        public NodeWrapper(Node node, int totalCost) {
            this.node = node;
            this.totalCost = totalCost;
        }
    }
}