import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

public class Map {

    // holds map and player/enemy data for the loaded level and handles interactions
    // on the map
    private String map[][];
    private int maxX;
    private int maxY;
    private int numEnemy;
    private String mapOne[];

    private Player p1;
    // Array of enemies
    int enemyPos[][];

    public Map() throws FileNotFoundException {
        maxX = 50;
        maxY = 20;
        map = new String[50][20];
        mapOne = new String[20];

        // Create enemies on the map
        numEnemy = 4;
        enemyPos = new int[4][2];
        enemyPos[0][0] = 48;
        enemyPos[1][0] = 9;
        enemyPos[2][0] = 21;
        enemyPos[3][0] = 3;
        enemyPos[0][1] = 18;
        enemyPos[1][1] = 1;
        enemyPos[2][1] = 8;
        enemyPos[3][1] = 14;

        p1 = new Player(1, 1);

        File file = new File("map.txt");
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            for (int i = 0; i < 20; i++) {
                mapOne[i] = sc.nextLine();
            }
        }

        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 20; j++) {
                map[i][j] = String.valueOf(mapOne[j].charAt(i));
            }
        }
    }

    // Print characters from map array to terminal using screen
    public void PrintMap(Screen screen) {
        int pX = p1.getX();
        int pY = p1.getY();

        screen.clear();

        // DONT ACCESS ARRAY AT [i][j] its backwards do [j][i]
        for (int i = 0; i < maxY; i++) {
            for (int j = 0; j < maxX; j++) {
                // Print player character yellow
                if (map[j][i].equals("@")) {
                    screen.putString(j + j, i, map[j][i], Terminal.Color.YELLOW, Terminal.Color.BLACK);
                    screen.putString(j + j + 1, i, map[j][i], Terminal.Color.YELLOW, Terminal.Color.BLACK);
                } else {
                    if (Math.abs(j - pX) < 100 && Math.abs(i - pY) < 100) {
                        // Print enemies red EE
                        if (map[j][i].equals("E")) {
                            screen.putString(j + j, i, map[j][i], Terminal.Color.RED, Terminal.Color.BLACK);
                            screen.putString(j + j + 1, i, map[j][i], Terminal.Color.RED, Terminal.Color.BLACK);
                        }
                        // Print walls white
                        else {
                            screen.putString(j + j, i, map[j][i], Terminal.Color.WHITE, Terminal.Color.BLACK);
                            screen.putString(j + j + 1, i, map[j][i], Terminal.Color.WHITE, Terminal.Color.BLACK);
                        }
                    }
                    // Only show blocks that are within 5 space of the character. Simulates using a
                    // lantern in a dark room (CYAN = dark, WHITE = illuminated)
                    else {
                        screen.putString(j + j, i, "#", Terminal.Color.CYAN, Terminal.Color.BLACK);
                        screen.putString(j + j + 1, i, "#", Terminal.Color.CYAN, Terminal.Color.BLACK);
                    }
                }
            }
            // The double print statements are used to make the spacing between the x and
            // y-axis even
        }
        printPlayerHUD(screen, 100, 30, 10);// Should be changed to be dynamic
        screen.refresh();
    }

    // The max size that the terminal can display
    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    // movePlayerXXXXX() move player +- 1 space in the desired direction
    // updates the data in the player class and on the map
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

    // Current player x and y coordinate
    public int getPlayerX() {
        return p1.getX();
    }

    public int getPlayerY() {
        return p1.getY();
    }

    // String held in the map array at a given coordinate pair
    public String getStringAt(int x, int y) {
        return map[x][y];
    }

    // Creates the HUD that keeps the user updated on the player's data
    // screenX is the width of the terminal and screenY is the height
    // leftUnderMap is the height that is available under the map after it is
    // printed
    public void printPlayerHUD(Screen screen, int screenX, int screenY, int leftUnderMap) {
        for (int i = 0; i < screenX; i++) {
            // Print bottom line
            screen.putString(i, screenY - 1, "#", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.putString(i, screenY - leftUnderMap, "#", Terminal.Color.WHITE, Terminal.Color.BLACK);
        }
        for (int i = 1; i <= leftUnderMap; i++) {
            // Print side lines and divider
            screen.putString(0, screenY - i, "#", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.putString(screenX - 1, screenY - i, "#", Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.putString(screenX / 5, screenY - i, "#", Terminal.Color.WHITE, Terminal.Color.BLACK);
        }
        for (int i = 3; i < (screenX - (screenX / 5) - 4); i++) {
            // Print health bar container
            // its a mess, make better later?
            // depends it I lock the terminal size or allow user to specify its size
            screen.putString((screenX / 5) + i, (screenY - (leftUnderMap / 2)) + 1, "#", Terminal.Color.WHITE,
                    Terminal.Color.BLACK);
            screen.putString((screenX / 5) + i, (screenY - (leftUnderMap / 2)) - 1, "#", Terminal.Color.WHITE,
                    Terminal.Color.BLACK);
            screen.putString((screenX / 5) + 3, (screenY - (leftUnderMap / 2)), "#", Terminal.Color.WHITE,
                    Terminal.Color.BLACK);
            screen.putString((screenX / 5) + (screenX - (screenX / 5) - 5), (screenY - (leftUnderMap / 2)), "#",
                    Terminal.Color.WHITE, Terminal.Color.BLACK);
            screen.putString((screenX / 5) + 3, (screenY - (leftUnderMap / 2)) - 2, "Health", Terminal.Color.WHITE,
                    Terminal.Color.BLACK);
        }
        double ratio = ((double) p1.getHealth()) / 100;
        for (int i = 4; i < ((screenX - (screenX / 5) - 5) * ratio); i++) {
            // Print health colored bar
            screen.putString((screenX / 5) + i, (screenY - (leftUnderMap / 2)), "#", Terminal.Color.RED,
                    Terminal.Color.BLACK);
        }
    }

    // returns the number of enemies that the map contains
    public int getNumEnemy() {
        return numEnemy;
    }

    // Set the string held at a given map coordinate
    public void setString(String set, int x, int y) {
        map[x][y] = set;
    }

    // Allows access to player health data
    public void hurtPlayer() {
        p1.setHealth(-10);
    }

    public int getPlayerHealth() {
        return p1.getHealth();
    }

}