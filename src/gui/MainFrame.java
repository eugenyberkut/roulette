package gui;

import server.Main;

import javax.swing.*;
import java.awt.*;

/**
 * Главное окно - табло рулетки и кнопка управления
 */
public class MainFrame extends JFrame{
    Main roulette;
    ScoreBoard scoreBoard;
    Timer timer = new Timer(10000, e -> {
        doGame();
    });

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
        JPanel bottomPanel = new JPanel();
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        JButton startStopButton = new JButton("Start/Stop");
        bottomPanel.add(startStopButton);
        startStopButton.addActionListener(e -> {
            if (roulette.getServerState().equals(Main.REGISTRATION)) {
                roulette.setServerState(Main.RUNNING);
                setTitle("Roulette - running");
            } else {
                roulette.setServerState(Main.GAME_OVER);
                setTitle("Roulette - stopped");
            }
        });

        JButton gameButton = new JButton("Game!");
        bottomPanel.add(gameButton);
        gameButton.addActionListener(e -> {
            doGame();
        });


        JButton autoGameButton = new JButton("Auto game");
        bottomPanel.add(autoGameButton);
        autoGameButton.addActionListener(e -> {
            if (timer.isRunning()) {
                timer.stop();
                setTitle("Roulette - running");
            } else {
                timer.start();
                setTitle("Roulette - auto running");
            }
        });
    }

    private void doGame() {
        roulette.doGame();
        repaint();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

}
