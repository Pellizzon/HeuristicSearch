package br.insper.robot19;

import java.awt.*;
import java.io.IOException;

public class MapGreedy {
    GridMap map;
    int width;
    int height;
    Canvas screen;
    // BufferedImage image;

    /**
     *
     * @param g Object of type GridMap
     */
    public MapGreedy(GridMap g) {
        this(g, 800, 600);
    }

    /**
     *
     * @param g      Object of type GridMap
     * @param width  Window width
     * @param height Window height
     */
    public MapGreedy(GridMap g, int width, int height) {
        this.map = g;
        this.width = width;
        this.height = height;
        screen = new Canvas("Path Finder", this.width, this.height, Color.lightGray);

    }

    /**
     * Draws a visual representation of the input map
     */
    public void draw() {
        int h = map.getHeight();
        int w = map.getWidth();

        int sqx = this.width / w;
        int sqy = this.height / h;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {

                switch (map.getBlockType(i, j)) {
                    case FREE:
                        screen.setForegroundColor(Color.WHITE);
                        break;
                    case WALL:
                        screen.setForegroundColor(Color.BLACK);
                        break;
                    case SAND:
                        screen.setForegroundColor(Color.YELLOW);
                        break;
                    case METAL:
                        screen.setForegroundColor(Color.BLUE);
                        break;
                }

                screen.fillRectangle(j * sqx, i * sqy, sqx - 2, sqy - 2);
            }
        }

        int[] goal = map.getGoal();
        int[] start = map.getStart();

        screen.setForegroundColor(Color.GREEN);
        screen.fillCircle(start[1] * sqx + sqx / 4, start[0] * sqy + sqy / 4, sqx / 2);

        screen.setForegroundColor(Color.RED);
        screen.fillCircle(goal[1] * sqx + sqx / 4, goal[0] * sqy + sqy / 4, sqx / 2);

    }

    /**
     * Saves a png file with what's shown in the Canvas
     */
    public void saveFile(String filename) {
        screen.saveFile(filename);
    }

    /**
     *
     */
    public void setAndDrawAnswer(RobotAction[] answer) {
        if (answer == null) {
            System.out.println("No answer found for this problem.");
        } else {
            int[] s = map.getStart();
            Block current = new Block(s[0], s[1], map.getBlockType(s[0], s[1]));
            System.out.print("Answer: ");
            for (RobotAction a : answer) {
                System.out.print(", " + a);
                Block next = map.nextBlock(current, a);
                map.setRoute(next.row, next.col);
                plotStep(current, next);
                current = next;
            }
        }

    }

    /**
     * Draws line between two blocks
     * 
     * @param current
     * @param next
     */
    public void plotStep(Block current, Block next) {
        int h = map.getHeight();
        int w = map.getWidth();

        int sqx = this.width / w;
        int sqy = this.height / h;

        screen.setForegroundColor(Color.GREEN);
        screen.drawLine(current.col * sqx + sqx / 2, current.row * sqy + sqy / 2, next.col * sqx + sqx / 2,
                next.row * sqy + sqy / 2);

    }

    public static void main(String[] args) {
        GridMap map = null;
        try {
            map = GridMap.fromFile("map.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * Shows the input map initial state
         */
        MapGreedy fig = new MapGreedy(map);
        fig.draw();
        fig.saveFile("GreedySearch.png");

        // Initial block
        int s[] = map.getStart();
        Block sb = new Block(s[0], s[1], map.getBlockType(s[0], s[1]));

        // Final block
        int f[] = map.getGoal();
        Block g = new Block(f[0], f[1], map.getBlockType(f[0], f[1]));

        // Running the search algorithm
        GreedySearch search = new GreedySearch(map, sb, g);
        RobotAction[] answer = search.solve();

        // Requesting the plot of the answer
        fig.setAndDrawAnswer(answer);
        fig.saveFile("GreedySolved.png");

    }
}
