import java.io.FileNotFoundException;
import java.io.Serializable;

public class Player implements Serializable {
    private int posX;
    private int posY;
    private int health;
    static Map map;
    public String name;
    public int ClientNum;
    public int score;

    /*
     * public Player(int posX, int posY) {
     * this.posX = posX;
     * this.posY = posY;
     * score = 0;
     * }
     */

    /*
     * Constructor used to make players in Map.java
     */
    public Player() {
        score = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
    }

    // Standard get and set methods for player attributes
    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public void setPosX(int x) {
        posX = x;
    }

    public void setPosy(int y) {
        posY = y;
    }

    public int getScore() {
        return score;
    }

    public void incScore() {
        score++;
    }

}
