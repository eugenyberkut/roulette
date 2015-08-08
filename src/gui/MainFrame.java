package gui;

import server.Main;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.util.Properties;

/**
 * Главное окно - табло рулетки и кнопка управления
 */
public class MainFrame extends JFrame{
    Main roulette;
    ScoreBoard scoreBoard;
    Timer timer = new Timer(5000, e -> doGame());

    public MainFrame() {
        setTitle("Roulette");
        roulette = new Main();
        roulette.setServerState("registration");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initComponents();
        roulette.start();
        roulette.setScoreBoard(scoreBoard);
        pack();
    }

    private void initComponents() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader("roulette.ini"));
            int port = Integer.parseInt(properties.getProperty("port", "666"));
            int startMoney = Integer.parseInt(properties.getProperty("startmoney", "500"));
            int maxBet = Integer.parseInt(properties.getProperty("maxbet", "100"));
            int delay = Integer.parseInt(properties.getProperty("delay", "5000"));
            timer.setDelay(delay);
            roulette.setProps(port, startMoney, maxBet);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(0);
        }
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
                timer.stop();
                setTitle("Roulette - stopped");
            }
        });

        JButton gameButton = new JButton("Game!");
        bottomPanel.add(gameButton); 
        gameButton.addActionListener(e -> doGame());

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
