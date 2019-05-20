package br.insper.robot19;

import java.util.Comparator;

public class ComparatorGulosa implements Comparator<Node> {

    public int compare(Node b1, Node b2) {

        if (b1.getHeuristic() < b2.getHeuristic()) {
            return -1;
        } else if (b1.getHeuristic() > b2.getHeuristic()) {
            return 1;
        } else {
            return 0;
        }
    }
}
