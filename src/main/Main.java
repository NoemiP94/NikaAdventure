package main;

import javax.swing.JFrame;


public class Main {

    public static JFrame window;
    public static void main(String[] args) {
        //create a window
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //lets the window properly close when user clicks the close button
        window.setResizable(false); //not resize the window
        window.setTitle("Nika's Adventures");
        //window.setUndecorated(true);

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); //fit the preferred size and layouts of GamePanel

        window.setLocationRelativeTo(null); //the window will be displayed at the center of the screen
        window.setVisible(true); //to see the window

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}
