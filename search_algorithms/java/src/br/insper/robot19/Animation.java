package br.insper.robot19;

import java.awt.*;

public class Animation {
    GridMap map;
    int width;
    int height;
    Canvas screen;
    // BufferedImage image;

    /**
     *
     * @param g Object of type GridMap
     */
    public Animation(GridMap g) {
        this(g, 800, 600);
    }

    /**
     *
     * @param g      Object of type GridMap
     * @param width  Window width
     * @param height Window height
     */
    public Animation(GridMap g, int width, int height) {
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

    public void draw_visited(Block b) {
        int h = map.getHeight();
        int w = map.getWidth();

        int sqx = this.width / w;
        int sqy = this.height / h;

        Color cyan = new Color(16, 196, 196);
        screen.setForegroundColor(cyan);
        screen.fillRectangle(b.col * sqx + sqx / 4, b.row * sqy + sqy / 4, sqx / 2, sqy / 2);
    }

    public void draw_frontier(Block b) {
        int h = map.getHeight();
        int w = map.getWidth();

        int sqx = this.width / w;
        int sqy = this.height / h;

        Color purple = new Color(67, 9, 122);
        screen.setForegroundColor(purple);
        screen.fillRectangle(b.col * sqx + sqx / 4, b.row * sqy + sqy / 4, sqx / 2, sqy / 2);
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
    public void setAndDrawSolucao(RobotAction[] ans) {
        if (ans == null) {
            System.out.println("No answer found for this problem.");
        } else {
            int[] s = map.getStart();
            Block atual = new Block(s[0], s[1], map.getBlockType(s[0], s[1]));
            System.out.print("Answer: ");
            for (RobotAction a : ans) {
                System.out.print(", " + a);
                Block next = map.nextBlock(atual, a);
                map.setRoute(next.row, next.col);
                plotStep(atual, next);
                atual = next;
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
}
