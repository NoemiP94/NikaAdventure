package main;

import javax.swing.*;


public class Main {

    public static JFrame window;
    public static void main(String[] args) {
        //create a window
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //lets the window properly close when user clicks the close button
        window.setResizable(false); //not resize the window
        window.setTitle("Nika's Adventures");
        new Main().setIcon(); //change window icon

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        gamePanel.config.loadConfig();
        if(gamePanel.fullScreenOn == true){
            window.setUndecorated(true);
        }

        window.pack(); //fit the preferred size and layouts of GamePanel

        window.setLocationRelativeTo(null); //the window will be displayed at the center of the screen
        window.setVisible(true); //to see the window

        gamePanel.setupGame();
        gamePanel.startGameThread();
    }

    public void setIcon(){
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("player/nika_down-1.png"));
        window.setIconImage(icon.getImage());
    }
}
