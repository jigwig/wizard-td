package WizardTD;

import java.util.*;
import processing.core.PVector;

public class Pathfinder {

    private class Node {
        public int x, y, depth;
        public Node parent;

        public Node(int x, int y, int depth) {
            this.x = x;
            this.y = y;
            this.depth = depth;
        }
    }

    private Board board;
    private List<List<PVector>> allPaths;

    public Pathfinder(Board board) {
        this.board = board;
        this.allPaths = generateAllPaths();
    }

    public List<PVector> getRandomPath() {
        if (allPaths.isEmpty()) {
            return null;
        }
        int randomIndex = new Random().nextInt(allPaths.size());
        return allPaths.get(randomIndex);
    }

    private List<List<PVector>> generateAllPaths() {
        List<List<PVector>> allPaths = new ArrayList<>();
        List<PVector> startNodes = findStartNodes();
        
        for (PVector start : startNodes) {
            Queue<Node> queue = new LinkedList<>();
            Set<PVector> visited = new HashSet<>();
            queue.add(new Node((int) start.x, (int) start.y, 0));
            
            int minDepth = Integer.MAX_VALUE;
            
            while (!queue.isEmpty()) {
                Node current = queue.poll();
                if (current.depth > minDepth) { 
                    continue; 
                }

                visited.add(new PVector(current.x, current.y));
                PVector[] directions = {
                    new PVector(1, 0),
                    new PVector(-1, 0),
                    new PVector(0, 1),
                    new PVector(0, -1)
                };

                for (PVector dir : directions) {
                    int newX = current.x + (int) dir.x;
                    int newY = current.y + (int) dir.y;

                    if (newX < 0 || newY < 0 || newX >= App.BOARD_WIDTH || newY >= App.BOARD_WIDTH) continue;

                    PVector nextPos = new PVector(newX, newY);
                    if (visited.contains(nextPos)) continue;

                    Node nextNode = new Node(newX, newY, current.depth + 1);
                    nextNode.parent = current;

                    char tile = board.getBoardLayout()[newY][newX];

                    if (tile == 'W' && nextNode.depth <= minDepth) {
                        minDepth = nextNode.depth;
                        List<PVector> path = new ArrayList<>();
                        buildPath(path, nextNode);
                        allPaths.add(path);
                    } else if (tile == 'X') {
                        queue.add(nextNode);
                    }
                }
            }
        }
        
        return allPaths;
    }

    private void buildPath(List<PVector> path, Node endNode) {
        Node current = endNode;
        while (current != null) {
            path.add(0, new PVector(current.x, current.y));
            current = current.parent;
        }
    }

    private List<PVector> findStartNodes() {
        List<PVector> startNodes = new ArrayList<>();
        char[][] layout = board.getBoardLayout();
        for (int y = 0; y < layout.length; y++) {
            for (int x = 0; x < layout[0].length; x++) {
                if (layout[y][x] == 'X') {
                    if (x == 0) {
                        startNodes.add(new PVector(-1, y));
                    } else if (x == layout[0].length - 1) {
                        startNodes.add(new PVector(layout[0].length, y));
                    } else if (y == 0) {
                        startNodes.add(new PVector(x, -1));
                    } else if (y == layout.length - 1) {
                        startNodes.add(new PVector(x, layout.length));
                    }
                }
            }
        }
        return startNodes;
    }
}
