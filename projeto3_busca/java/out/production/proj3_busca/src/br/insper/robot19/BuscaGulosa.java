package br.insper.robot19;

import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class BuscaGulosa {

    private Block start;
    private Block end;
    private GridMap map;

    private PriorityQueue<Node> border;

    public BuscaGulosa(GridMap map, Block start, Block end) {
        this.map = map;
        this.start = start;
        this.end = end;
    }

    public Node buscar() {

        Node root = new Node(start, null, null, 0, map);

        border = new PriorityQueue<Node>(144, new ComparatorGulosa());
        border.add(root);

        while(!border.isEmpty()) {

            Node node = border.remove();
            Block atual = node.getValue();

            if(atual.row == end.row && atual.col == end.col) {
                return node;

            } else for(RobotAction acao : RobotAction.values()) {

                Block proximo = map.nextBlock(atual, acao);

                if(proximo != null && proximo.type != BlockType.WALL) {
                    Node novoNode = new Node(proximo, node, acao, proximo.type.cost, map);
                    border.add(novoNode);
                }
            }
        }
        return null;
    }

    public RobotAction[] resolver() {

        // Encontra a solução através da busca
        Node destino = buscar();
        if(destino == null) {
            return null;
        }

        //Faz o backtracking para recuperar o caminho percorrido
        Node atual = destino;
        Deque<RobotAction> caminho = new LinkedList<RobotAction>();
        while(atual.getAction() != null) {
            caminho.addFirst(atual.getAction());
            atual = atual.getParent();
        }
        return caminho.toArray(new RobotAction[caminho.size()]);
    }
}
