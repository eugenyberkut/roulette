package gui;

import server.Main;

import javax.swing.*;
import java.awt.*;

/**
 * Created by eugen on 04.08.2015.
 */
public class MainFrame extends JFrame{
    Main roulette;
    ScoreBoard scoreBoard;

    public MainFrame() {
        setTitle("Roulette");
        roulette = new Main();
        roulette.setServerState("registration");
        roulette.start();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initComponents();
        roulette.setScoreBoard(scoreBoard);
        pack();
    }

    private void initComponents() {
        getContentPane().add(scoreBoard = new ScoreBoard(roulette));
    }


    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

}
