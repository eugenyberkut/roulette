package gui;

import server.Main;
import server.UserAccount;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugen on 04.08.2015.
 */
public class ScoreBoard extends JPanel {
    List<UserAccount> accountList;

    public ScoreBoard(Main main) {
        accountList = main.getUserAccounts();
//        accountList.add(new UserAccount("1","1111",500));
//        accountList.add(new UserAccount("2","2111",500));
//        accountList.add(new UserAccount("3","3111",500));
//        accountList.add(new UserAccount("4","4111",500));
//        accountList.add(new UserAccount("5","5111",500));
    }

    public void setAccountList(List<UserAccount> accountList) {
        this.accountList = accountList;
    }

    Color[] colors = {Color.BLUE, Color.ORANGE, Color.CYAN, Color.PINK, Color.WHITE, Color.YELLOW, Color.RED, Color.GREEN, Color.MAGENTA, Color.yellow.darker().darker()};

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        //g.drawLine(0,0,getWidth(),getHeight());
        int h = getHeight() / 10;
        for (int  i=0; i<accountList.size(); i++) {
            g.setColor(Color.BLACK);
            g.fillRect(75, h * i + h / 4 + 5, accountList.get(i).getMoney(), h / 2);
            g.setColor(colors[i]);
            g.fillRect(70, h*i + h/4, accountList.get(i).getMoney(), h/2);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }
}
