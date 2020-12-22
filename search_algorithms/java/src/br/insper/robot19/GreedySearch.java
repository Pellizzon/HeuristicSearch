package br.insper.robot19;

import java.util.*;

public class GreedySearch {

    private Block start;
    private Block end;
    private GridMap map;

    private Animation animation;
    private int count = 1;

    private PriorityQueue<Node> border;

    public GreedySearch(GridMap map, Block start, Block end) {
        this.map = map;
        this.start = start;
        this.end = end;
        this.animation = new Animation(map);
        this.animation.draw();
        this.animation.saveFile("Frames/Greedy/Frame0.png");
    }

    public Node search() {

        Node root = new Node(start, null, null, 0, map);
        Set<Block> visited = new HashSet<>();

        border = new PriorityQueue<Node>(144, new ComparatorGreedy());
        border.add(root);
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
            animation.saveFile("Frames/Greedy/Frame" + count + ".png");
            count++;
        }
        return null;
    }

    public RobotAction[] solve() {

        // Encontra a solução através da busca
        Node destino = search();
        if (destino == null) {
            return null;
        }

        // Faz o backtracking para recuperar o caminho percorrido
        Node current = destino;
        Deque<RobotAction> caminho = new LinkedList<RobotAction>();
        while (current.getAction() != null) {
            caminho.addFirst(current.getAction());
            current = current.getParent();
        }
        animation.setAndDrawSolucao(caminho.toArray(new RobotAction[caminho.size()]));
        animation.saveFile("Frames/Greedy/Answer.png");
        return caminho.toArray(new RobotAction[caminho.size()]);
    }
}
