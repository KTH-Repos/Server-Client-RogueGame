import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;

/* This class represents the client. 
*/
public class Game {
    private static final String SERVER = "localhost";
    private static final int PORT = 9000;
    private static BufferedReader in; // get input from server
    private static PrintWriter out; // send output to server
    private static Socket server; // the server
    private static Map map; // playing field of game

    public static void main(String[] args) {
        // Create playing field of game before game starts
        map = new Map();
        map.loadMap();
        map.printMap();

        try {
            server = new Socket(SERVER, PORT); // connect to server
            gameLoop(server, map); // start game
        } catch (IOException e) {
            map.message = "Server Not Found Please Start Server and Restart Game";
            map.printMap();
        }
    }

    /*
     * Game loop that takes input from server and acts according to it
     */
    private static void gameLoop(Socket server, Map map) {
        Boolean playing = true; // true as long as game is run
        OutputStream outstream; // used to send binary data to server
        try {
            // used to receive binary data from
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            outstream = server.getOutputStream();
            out = new PrintWriter(outstream, true);

            String response = in.readLine(); // received data from server saved in string
            if (response.startsWith("WELCOME")) {
                // the game is ready to be run
                System.out.println("Starting Game");
            }
            // code to manage input and change state of game locally
            while (playing) {
                // if buffer is ready to be read
                if (in.ready()) {
                    response = in.readLine(); // save buffered input in response
                    System.out.println(response);
                    // code to handle position and implement new changes in playing field of game
                    if (response.startsWith("POS")) {
                        setPlayerPosition(map, response);
                    }
                    // code to handle position of opponent and implement new changes in playing
                    // field of game
                    if (response.startsWith("OPP")) {
                        setOpponentPosition(map, response);
                    }
                    // code to handle opponent's score and implement new changes in playing field of
                    // game
                    if (response.startsWith("OSCO")) {
                        map.p2.score = Integer.parseInt(response.split(",")[1]);
                    }
                    // code to handle any type of message, such as quit
                    if (response.startsWith("MSG")) {
                        map.message = response.split(",")[1];
                    }
                }
                // read input from client and make movement in playing field
                movementController();

                // code to handle the game objective
                // if all treasure picked up by players is 11, then game is over
                if (map.p1.score + map.p2.score == 11) {
                    playing = false; // game is over
                    // if player1 one has more points
                    if (map.p1.score > map.p2.score) {
                        map.gameOver = "You win!!!";
                        Thread.sleep(500);
                        server.close();
                        // if player2 has more points
                    } else if (map.p1.score < map.p2.score) {
                        map.gameOver = "You Lose!!";
                        Thread.sleep(500);
                        server.close();
                        // if there is disconnection, game is over without any winners
                    } else {
                        map.gameOver = "Game ended on uncertain conditions";
                        Thread.sleep(500);
                        server.close();
                    }
                }
                Thread.sleep(200);
                map.printMap(); // update playing field of game as long as game is running
            }
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*
     * Set the position of a player in playing field after movement
     * and send the new position to server
     */
    private static void setPlayerPosition(Map map, String pos) {
        String[] args = pos.split(",");
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        map.setString(" ", map.getPlayerX(), map.getPlayerY());
        map.p1.setPosX(x);
        map.p1.setPosy(y);
        map.setString("@", x, y);
        out.println("NPOS" + "," + map.getPlayerX() + "," + map.getPlayerY());
        map.printMap();
    }

    /*
     * Set the position of opponent in playing field after movement
     * and send new position to server
     */
    private static void setOpponentPosition(Map map, String pos) {
        String[] args = pos.split(",");
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        map.setString(" ", map.p2.getX(), map.p2.getY());
        map.p2.setPosX(x);
        map.p2.setPosy(y);
        map.setString("$", x, y);
        map.printMap();
    }

    /*
     * Return true if a move in playing field is possible or not
     */
    public static boolean validMove(String move) {
        int posX = map.getPlayerX();
        int posY = map.getPlayerY();
        int maxX = map.getMaxX();
        int maxY = map.getMaxY();
        String nextTile;
        switch (move) {
            case "right":
                nextTile = map.getStringAt(posX + 1, posY);
                break;

            case "left":
                nextTile = map.getStringAt(posX - 1, posY);
                break;

            case "down":
                nextTile = map.getStringAt(posX, posY + 1);
                break;

            case "up":
                nextTile = map.getStringAt(posX, posY - 1);
                break;

            default:
                nextTile = "#";
                break;
        }
        // if there is wall or another player, move is not valid
        if (nextTile.equals("#") || nextTile.equals("$")) {
            System.out.println("Wall or Enemy Found");
            return false;
        }
        // if there is treasure, move is valid and score is increased
        if (nextTile.equals("G")) {
            map.p1.score++;
            out.println("SCO" + "," + map.p1.score);
            return true;
        }
        return true;
    }

    /*
     * Read keyboard input and perform movement in playing field
     * 
     */
    public static void movementController() {
        Key key = map.screen.readInput();

        // Move around with arrow keys in normal map view escape closes the application
        try {
            switch (key.getKind()) {
                case Escape:
                    try {
                        // if player wants to disconnect, ESC is pressed and socket is closed
                        out.println("QUIT");
                        out.flush();
                        server.close();
                        map.screen.stopScreen();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case ArrowRight:
                    if (validMove("right")) {
                        setPlayerPosition(map, "POS" + "," + (map.p1.getX() + 1) + "," + map.p1.getY());
                    }
                    break;
                case ArrowLeft:
                    if (validMove("left")) {
                        setPlayerPosition(map, "POS" + "," + (map.p1.getX() - 1) + "," + map.p1.getY());
                    }
                    break;

                case ArrowDown:
                    if (validMove("down")) {
                        setPlayerPosition(map, "POS" + "," + map.p1.getX() + "," + (map.p1.getY() + 1));
                    }
                    break;

                case ArrowUp:
                    if (validMove("up")) {
                        setPlayerPosition(map, "POS" + "," + map.p1.getX() + "," + (map.p1.getY() - 1));
                    }
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e) {

        }
    }

}
