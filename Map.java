import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

/* The playing field of the game and implements the graphics library "Lanterna"
   The field is read from "map.txt" and is printed before game starts
 */
public class Map {
    public String map[][]; // holds chars from map.txt
    private int maxX; // width of playing field
    private int maxY; // height of playing field
    private String mapLine[]; // temporarily holds chars from map.txt before printing to terminal
    // private ArrayList players; // holds active players
    Screen screen; // outputs the playing field
    Player p1; // player one
    Player p2; // player two
    String gameOver = "";
    String message = "";

    public Map() {
        this.screen = TerminalFacade.createScreen(); // create screen object
        this.screen.startScreen(); // start the screen
        System.out.println("Created Screen");
        // create two players
        p1 = new Player();
        p2 = new Player();
    }

    /*
     * Reads map.txt and stores result in map before screen prints
     */
    public void loadMap() {
        this.maxX = 50;
        this.maxY = 20;
        this.map = new String[50][20];
        this.mapLine = new String[20];
        // this.players = new ArrayList<>();

        File file = new File("map.txt");
        Scanner sc;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                for (int i = 0; i < 20; i++) {
                    mapLine[i] = sc.nextLine();
                }
            }

            for (int i = 0; i < 50; i++) {
                for (int j = 0; j < 20; j++) {
                    map[i][j] = String.valueOf(mapLine[j].charAt(i));
                }
            }
            System.out.println("Done Loading Map from File");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Map file not found");
            e.printStackTrace();
        }

    }

    /*
     * Set a string in specified place in map
     */
    public void setString(String set, int x, int y) {
        map[x][y] = set;
    }

    /*
     * Prints the playing field by reading from map, prints scores and messages as
     * well
     */
    public void printMap() {
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                switch (map[j][i]) {
                    case "@":
                        this.screen.putString(j + j, i, map[j][i], Terminal.Color.YELLOW, Terminal.Color.BLACK);
                        this.screen.putString(j + j + 1, i, map[j][i], Terminal.Color.YELLOW, Terminal.Color.BLACK);
                        break;

                    case "$":
                        this.screen.putString(j + j, i, map[j][i], Terminal.Color.GREEN, Terminal.Color.BLACK);
                        this.screen.putString(j + j + 1, i, map[j][i], Terminal.Color.GREEN, Terminal.Color.BLACK);
                        break;

                    case "G":
                        this.screen.putString(j + j, i, map[j][i], Terminal.Color.GREEN, Terminal.Color.BLACK);
                        break;

                    default:
                        this.screen.putString(j + j, i, map[j][i], Terminal.Color.WHITE, Terminal.Color.BLACK);
                        this.screen.putString(j + j + 1, i, map[j][i], Terminal.Color.WHITE, Terminal.Color.BLACK);
                        break;
                }
            }
        }
        printScores();
        printMessage();
        this.screen.refresh();
    }

    /*
     * Return the width of playing field
     */
    public int getMaxX() {
        return maxX;
    }

    /*
     * Return the height of playing field
     */
    public int getMaxY() {
        return maxY;
    }

    /*
     * Draw player in playing field after movement
     */
    public void movePlayerRight() {
        int pX = p1.getX();
        int pY = p1.getY();
        p1.setPosX(pX + 1);
        map[pX][pY] = " ";
        map[pX + 1][pY] = "@";
    }

    public void movePlayerLeft() {
        int pX = p1.getX();
        int pY = p1.getY();
        p1.setPosX(pX - 1);
        map[pX][pY] = " ";
        map[pX - 1][pY] = "@";
    }

    public void movePlayerDown() {
        int pX = p1.getX();
        int pY = p1.getY();
        p1.setPosy(pY + 1);
        map[pX][pY] = " ";
        map[pX][pY + 1] = "@";
    }

    public void movePlayerUp() {
        int pX = p1.getX();
        int pY = p1.getY();
        p1.setPosy(pY - 1);
        map[pX][pY] = " ";
        map[pX][pY - 1] = "@";
    }

    /*
     * Return x-cord of player
     */
    public int getPlayerX() {
        return p1.getX();
    }

    /*
     * Return y-cord of player
     */
    public int getPlayerY() {
        return p1.getY();
    }

    /*
     * Return a String from playing field
     */
    public String getStringAt(int x, int y) {
        return map[x][y];
    }

    /*
     * Print the score or game status to screen
     */
    public void printScores() {
        if (gameOver.equals("")) {
            screen.putString(getMaxX() / 2, 24, "Scores", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.putString(getMaxX() / 2, 25, "You: " + p1.getScore(), Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.putString(getMaxX() / 2, 26, "You Opponent: " + p2.getScore(), Terminal.Color.WHITE,
                    Terminal.Color.BLACK);
        } else {
            screen.putString(getMaxX() / 2, 24, gameOver, Terminal.Color.RED, Terminal.Color.YELLOW);
        }

    }

    /*
     * Print message to screen, such as "Opponent disconnected"
     */
    public void printMessage() {
        if (!message.equals("")) {
            screen.putString(getMaxX() / 2, 28, message, Terminal.Color.RED, Terminal.Color.YELLOW);
        }
    }
}
