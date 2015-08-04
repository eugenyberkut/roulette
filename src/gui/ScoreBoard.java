package gui;

import server.UserAccount;

import javax.swing.*;
import java.awt.*;

/**
 * Created by eugen on 04.08.2015.
 */
public class ScoreBoard extends JPanel {
    UserAccount ua;

    public void setUa(UserAccount ua) {
        this.ua = ua;
    }

    Color[] colors = {Color.BLUE, Color.ORANGE, Color.CYAN, Color.PINK, Color.WHITE, Color.YELLOW, Color.RED, Color.GREEN, Color.MAGENTA, Color.yellow.darker().darker()};

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawLine(0,0,getWidth(),getHeight());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }
}
