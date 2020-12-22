package br.insper.robot19;

import java.io.IOException;

public class ExecuteA {

    public static void main(String[] args) {

        // Loads the map from a file
        GridMap map;
        try {
            map = GridMap.fromFile("map.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (map == null)
            throw new RuntimeException("Unable to obtain discretized map.");

        // Prints map
        System.out.println("Map to analyze:");
        System.out.println(map);

        // Executes search
        int[] rowColIni = map.getStart();
        int[] rowColFim = map.getGoal();
        Block initial = new Block(rowColIni[0], rowColIni[1], BlockType.FREE);
        Block target = new Block(rowColFim[0], rowColFim[1], BlockType.FREE);
        A_Search search = new A_Search(map, initial, target);
        RobotAction[] ans = search.solve();

        // Shows Answer
        if (ans == null) {
            System.out.println("No answer found for this problem.");
        } else {

            Block current = initial;
            System.out.print("Answer: ");
            for (RobotAction a : ans) {
                System.out.print(", " + a);
                Block next = map.nextBlock(current, a);
                map.setRoute(next.row, next.col);
                current = next;
            }

            // Shows map with the route found
            System.out.println();
            System.out.println("Route found:");
            System.out.println(map);
        }
    }
}
