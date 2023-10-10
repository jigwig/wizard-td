package WizardTD;

import java.util.*;
import processing.core.PVector;

public class Pathfinder {

    private class Node
    {
        public int x;
        public int y;
        public Node parent;

        public Node(float x, float y)
        {
            this(Math.round(x), Math.round(y));
        }
        
        public Node(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }

    private Board board;
    private List<PVector> path;

    public Pathfinder(Board board) {
        this.board = board;
        this.path = generatePath();
    }

    public List<PVector> getPath() {
        return this.path;
    }

    private List<PVector> generatePath() {
        Queue<Node> queue = new LinkedList<>();
        Set<PVector> visited = new HashSet<>();
        PVector wizardPos = findWizardPosition();
    
        if (wizardPos != null) {
            queue.add(new Node(wizardPos.x, wizardPos.y));
            visited.add(wizardPos);
        }

        while (!queue.isEmpty()) {
            Node current = queue.remove();

            PVector[] directions = {
                new PVector(0, 1),
                new PVector(1, 0),
                new PVector(0, -1),
                new PVector(-1, 0)
            };

            for (PVector dir : directions) {
                PVector next = PVector.add(new PVector(current.x, current.y), dir);
                if (isValid(next) && !visited.contains(next)) {

                    int x = Math.round(next.x);
                    int y = Math.round(next.y);

                    if (x == 0 || x >= 20 || y == 0 || y >= 20)
                    {
                        List<PVector> res = new ArrayList<>();

                        res.add(next);

                        while (current != null)
                        {
                            res.add(new PVector(current.x, current.y));
                            current = current.parent;
                        }

                        return res;
                    }

                    Node n = new Node(next.x, next.y);
                    n.parent = current;
                    queue.add(n);
                    visited.add(next);
                }
            }
        }
        
        return null;
    }

    private PVector findWizardPosition() {
        char[][] layout = board.getBoardLayout();
        for (int y = 0; y < layout.length; ++y) {
            for (int x = 0; x < layout[0].length; ++x) {
                if (layout[y][x] == 'W') {
                    return new PVector(x, y);
                }
            }
        }
        return null;
    }

    private boolean isValid(PVector pos) {
        int x = (int) pos.x;
        int y = (int) pos.y;
        char[][] layout = board.getBoardLayout();
        return x >= 0 && x < layout[0].length && y >= 0 && y < layout.length && layout[y][x] == 'X';
    }
}
