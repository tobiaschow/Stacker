import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;

public class Main extends JPanel implements MouseListener, KeyListener, Runnable{

    // Game States:
    // 0 <- Home Screen
    // 1 <- Credit/Tutorial Screen
    // 2 <- Game Screen
    // 3 <- Game Over Screen
    public static int gameState = 0;

    // Images
    public static BufferedImage home;
    public static BufferedImage credit;
    public static BufferedImage game;
    public static BufferedImage gameover;
    public static BufferedImage smiley;

    // Mouse/Keyboard Events
    public static int mouseX;
    public static int mouseY;

    // Game Stats

    public static int score;
    public static int lives;
    public static int direction;
    public static int[][] board;
    public static int row;
    public static int leftPos;
    public static int rightPos;
    public static int leftBound;
    public static int rightBound;
    public static int frameCounter;
    public static int frameCounterMax;

    public void run() {
        while(true) {
            repaint();
            try {
                Thread.sleep(17);
            }
            catch(Exception e) {
                System.out.println("Timer Error");
            }
        }
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(gameState == 0){
            g.drawImage(home, 0, 0, null);
        }
        else if(gameState == 1){
            g.drawImage(credit, 0, 0, null);
        }
        else if(gameState == 2){
            g.drawImage(game, 0, 0, null);
            for (int r = 0; r < 10; r++){
                for (int c = 0; c < 10; c++){
                    if(board[r][c] == 1){
                        g.drawImage(smiley, c*40, r*40+120, null);
                    }
                }
            }
            g.setFont(new Font("Calibri", Font.PLAIN, 20));
            g.setColor(new Color(255, 255, 255));
            g.drawString("Score: " + score, 175, 20);
            frameCounter++;
            if(frameCounter == frameCounterMax){
                updateRow();
                frameCounter = 0;
            }
        }
        else if(gameState == 3){
            g.drawImage(gameover, 0, 0, null);
        }

    }

    public static void updateRow(){
        for(int c = 0; c < 10; c++){
            board[row][c] = 0;
        }
        if(direction == 1 && rightPos == 9){
            direction = -1;
        }

        if(direction == -1 && leftPos == 0){
            direction = 1;
        }

        leftPos += direction;
        rightPos += direction;

        for(int i = 0; i < lives; i++){
            board[row][leftPos+i]=1;
        }

    }

    public static void stack(){

        if (row == 9){
            frameCounter = 0;
            frameCounterMax -= 1;
            board[row][leftPos] = 1;
            board[row][leftPos+1] = 1;
            board[row][leftPos+2] = 1;
            leftBound = leftPos;
            rightBound = leftPos + 2;
            row--;
        }
        else{
            //If the left side is on the right side of bounds, OR
            //If the right side is on the left side of bounds -> Game Over
            if(leftPos > rightBound || leftPos+lives-1 < leftBound){
                gameState = 3;
            }
            //If left side is on left bound, AND
            //If right side is on right bound -> perfect stack
            else if(leftPos == leftBound && rightPos == rightBound){
                frameCounter = 0;
                frameCounterMax -= 1;
                for(int i = 0; i < lives; i++){
                    board[row][leftPos+i] = 1;
                }
                row--;
            }

            // Case: Partially out of bound
            else{
                // if leftPos is out of bound
                if(leftPos < leftBound){
                    score++;
                    for(int i = 0; i < lives; i++){
                        board[row][leftPos+i] = 0;
                    }
                    lives -= leftBound-leftPos;
                    frameCounter = 0;
                    frameCounterMax -= 1;
                    for(int i = 0; i < lives; i++){
                        board[row][leftBound+i] = 1;
                    }
                    leftPos = leftBound;
                    rightPos = leftPos+lives-1;
                    rightBound = rightPos;
                    row--;
                }
                // if rightPos is out of bound
                else{
                    for(int i = 0; i < lives; i++){
                        board[row][leftPos+i] = 0;
                    }
                    lives -= rightPos-rightBound;
                    frameCounter = 0;
                    frameCounterMax -= 1;
                    for(int i = 0; i < lives; i++){
                        board[row][rightBound-i] = 1;
                    }
                    rightPos = rightBound;
                    leftPos = rightPos - lives+1;
                    leftBound = leftPos;
                    row--;
                }
            }
            if (lives == 0 || row == -1){
                gameState = 3;
            }
        }
        score += lives;
    }

    public Main(){
        try{
            home = ImageIO.read(new File("home.png"));
            credit = ImageIO.read(new File("credit.png"));
            game = ImageIO.read(new File("game.png"));
            gameover = ImageIO.read(new File("gameover.png"));
            smiley = ImageIO.read(new File("smiley.png"));
        }
        catch (Exception e){
            System.out.println("Image Error");
        }

        setPreferredSize(new Dimension(400,600));
        this.setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        // When using Runnable, you have to
        // create a new Thread
        Thread thread = new Thread(this);
        thread.start();
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("Stacker");
        Main panel = new Main();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Mouse and Keyboard Methods
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {

        mouseX = e.getX();
        mouseY = e.getY();
        if(gameState == 0){
            if (120 <= mouseX && mouseX <= 280 && 500 <= mouseY && mouseY <= 550){
                gameState = 1;
            }
            else if (120 <= mouseX && mouseX <= 280 && 425 <= mouseY && mouseY <= 475){
                gameState = 2;
                score = 0;
                lives = 3;
                direction = 1;
                row = 9;
                board = new int[10][10];
                leftPos = 0;
                rightPos = 2;
                frameCounter = 0;
                frameCounterMax = 9;
                board[row][0] = 1;
                board[row][1] = 1;
                board[row][2] = 1;
            }
        }
        else if(gameState == 1){
            if (120 <= mouseX && mouseX <= 280 && 500 <= mouseY && mouseY <= 550){
                gameState = 0;
            }
        }
        else if(gameState == 3){
            if (120 <= mouseX && mouseX <= 280 && 500 <= mouseY && mouseY <= 550){
                gameState = 0;
            }
        }
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        if(gameState == 2) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                gameState = 3;
            }
            if(e.getKeyChar() == ' '){
                stack();
            }
        }
    }
    public void keyReleased(KeyEvent e) {}
}