package gui;

import server.Main;
import server.UserAccount;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Панель - табло игры
 */
public class ScoreBoard extends JPanel {
    List<UserAccount> accountList;

    public ScoreBoard(Main main) {
        accountList = main.getUserAccounts();
    }

    public static final Color[] colors = {
            Color.BLUE,
            Color.ORANGE,
            Color.CYAN,
            Color.PINK,
            Color.WHITE,
            Color.YELLOW,
            Color.RED,
            Color.GREEN,
            Color.MAGENTA,
            Color.YELLOW.darker().darker()};

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        int h = getHeight() / 10;
        for (int  i=0; i<accountList.size(); i++) {
            g.setColor(Color.BLACK);
            g.fillRect(75, h * i + h / 4 + 5, getMoneyBarLengthByAccount(i), h / 2);
            g.drawString(accountList.get(i).getName(), 5, h * i + 2*h / 3);
            g.drawString(accountList.get(i).getMoney() +"", 80+ getMoneyBarLengthByAccount(i), h * i + 2*h / 3);
            g.setColor(colors[i]);
            g.fillRect(70, h*i + h/4, getMoneyBarLengthByAccount(i), h/2);
        }
    }

    private int getMoneyBarLengthByAccount(int i) {
        return accountList.get(i).getMoney() / 2;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700,500);
    }
}
