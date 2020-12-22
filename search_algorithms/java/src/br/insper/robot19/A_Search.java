package br.insper.robot19;

import java.util.*;

public class A_Search {

    private Block start;
    private Block end;
    private GridMap map;

    private Animation animation;
    private int count = 1;

    private PriorityQueue<Node> border;

    public A_Search(GridMap map, Block start, Block end) {
        this.map = map;
        this.start = start;
        this.end = end;
        this.animation = new Animation(map);
        this.animation.draw();
        this.animation.saveFile("Frames/A-Star/Frame0.png");
    }

    public Node search() {

        Node root = new Node(start, null, null, 0, map);

        border = new PriorityQueue<Node>(144, new ComparatorA());
        border.add(root);

        Set<Block> visited = new HashSet<>();
        visited.add(root.getValue());

        while (!border.isEmpty()) {

            Node node = border.remove();
            Block current = node.getValue();
            visited.add(current);

            if (current.row == end.row && current.col == end.col) {
                animation.draw_visited(current);
                return node;

            } else
                for (RobotAction action : RobotAction.values()) {

                    Block next = map.nextBlock(current, action);

                    if (next != null && next.type != BlockType.WALL) {
                        Node newNode = new Node(next, node, action, next.type.cost, map);
                        if (!visited.contains(newNode.getValue())) {
                            border.add(newNode);
                            animation.draw_frontier(newNode.getValue());
                        }
                    }
                    animation.draw_visited(current);
                }
            animation.saveFile("Frames/A-Star/Frame" + count + ".png");
            count++;
        }
        return null;
    }

    public RobotAction[] solve() {

        // Finds answer
        Node destination = search();
        if (destination == null) {
            return null;
        }

        // Does the backtracking to get the path used to reach the goal
        Node current = destination;
        Deque<RobotAction> path = new LinkedList<RobotAction>();
        while (current.getAction() != null) {
            path.addFirst(current.getAction());
            current = current.getParent();
        }
        animation.setAndDrawSolucao(path.toArray(new RobotAction[path.size()]));
        animation.saveFile("Frames/A-Star/Answer.png");
        return path.toArray(new RobotAction[path.size()]);
    }
}
