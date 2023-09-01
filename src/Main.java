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

    // Mouse/Keyboard Events
    public static int mouseX;
    public static int mouseY;

    public void run() {
        while(true) {
            repaint();
            try {
                Thread.sleep(80);
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
        }
        else if(gameState == 3){
            g.drawImage(gameover, 0, 0, null);
        }

    }

    public Main(){
        try{
            home = ImageIO.read(new File("home.png"));
            credit = ImageIO.read(new File("credit.png"));
            game = ImageIO.read(new File("game.png"));
            gameover = ImageIO.read(new File("gameover.png"));
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
        }
    }
    public void keyReleased(KeyEvent e) {}
}