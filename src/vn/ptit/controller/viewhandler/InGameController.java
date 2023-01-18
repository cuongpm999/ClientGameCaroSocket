/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ptit.controller.viewhandler;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import vn.ptit.ClientMain;
import vn.ptit.model.ChatItem;
import vn.ptit.model.Move;
import vn.ptit.model.Player;
import vn.ptit.model.Room;
import vn.ptit.utils.Avatar;
import vn.ptit.view.scene.InGameView;

/**
 *
 * @author Cuong Pham
 */
public class InGameController {

    private int giay = 0;
    private int phut = 0;
    private TimeCount timeCount;

    private InGameView inGameView;
    private Player player1;
    private Player player2;
    private int turn = 1;
    private String tick[][];
    private boolean flagTime = false;

    private Icon getIcon(String nameImg) {
        int width = 24, height = 24;
        Image image = new ImageIcon(getClass().getResource(
                "/vn/ptit/view/img/" + nameImg)).getImage();
        Icon icon = new ImageIcon(image.getScaledInstance(width, height,
                image.SCALE_SMOOTH));
        return icon;
    }

    private Icon getIconVs(String nameImg) {
        int width = 48, height = 48;
        Image image = new ImageIcon(getClass().getResource(
                "/vn/ptit/view/img/" + nameImg)).getImage();
        Icon icon = new ImageIcon(image.getScaledInstance(width, height,
                image.SCALE_SMOOTH));
        return icon;
    }

    public void reset() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                inGameView.getBtnOnBoard()[i][j].setBackground(Color.decode("#CCFFCC"));
                inGameView.getBtnOnBoard()[i][j].setIcon(null);
            }
        }
    }

    public InGameController(InGameView inGameView) {
        this.inGameView = inGameView;
        this.inGameView.addListener(new InGameListener());
        timeCount = new TimeCount();
        inGameView.getjLabel4().setText("Người chơi: " + ClientMain.socketHandler.readMessage.getPlayerLogin().getUsername());

    }

    public void setPlayer1(Room room) {
        String username = room.getPlayers().get(0).getUsername();
        inGameView.getLbPlayerNameId1().setText(username);
        inGameView.getLbAvatar1().setIcon(new ImageIcon(Avatar.PATH + room.getPlayers().get(0).getImg()));
        player1 = room.getPlayers().get(0);
    }

    public void setPlayer2(Room room) {
        String username = room.getPlayers().get(1).getUsername();
        inGameView.getLbPlayerNameId2().setText(username);
        inGameView.getLbAvatar2().setIcon(new ImageIcon(Avatar.PATH + room.getPlayers().get(1).getImg()));
        player2 = room.getPlayers().get(1);
    }

    public void addPointToCompetitor(int row, int column, String username) {
        if (username.equalsIgnoreCase(player1.getUsername()) && (turn == 1 || turn == 0)) {
            Icon icon = getIcon("icons8-delete-24.png");
            inGameView.getBtnOnBoard()[row][column].setBackground(Color.WHITE);
            inGameView.getBtnOnBoard()[row][column].setIcon(icon);

        }

        if (username.equalsIgnoreCase(player2.getUsername()) && (turn == 2 || turn == 0)) {
            Icon icon = getIcon("icons8-circle-24.png");
            inGameView.getBtnOnBoard()[row][column].setBackground(Color.WHITE);
            inGameView.getBtnOnBoard()[row][column].setIcon(icon);

        }
    }

    public void addPoint(int row, int column, String username) {
        if (username.equalsIgnoreCase(player1.getUsername()) && (turn == 1 || turn == 0)) {
            Icon icon = getIcon("icons8-delete-24.png");
            inGameView.getBtnOnBoard()[row][column].setBackground(Color.WHITE);
            inGameView.getBtnOnBoard()[row][column].setIcon(icon);
            ClientMain.socketHandler.sendMessage.move(new Move(row, column));
        }

        if (username.equalsIgnoreCase(player2.getUsername()) && (turn == 2 || turn == 0)) {
            Icon icon = getIcon("icons8-circle-24.png");
            inGameView.getBtnOnBoard()[row][column].setBackground(Color.WHITE);
            inGameView.getBtnOnBoard()[row][column].setIcon(icon);
            ClientMain.socketHandler.sendMessage.move(new Move(row, column));
        }
    }

    public void setTurn(String username) {
        if (player1.getUsername().equalsIgnoreCase(username)) {
            turn = 1;
            inGameView.getLbAvatar1().setBorder(javax.swing.BorderFactory.createTitledBorder("Đang đánh.."));
            inGameView.getLbAvatar2().setBorder(javax.swing.BorderFactory.createTitledBorder("Chờ"));
            Icon icon = getIconVs("icons8_sword_48px.png");
            inGameView.getLbVersus().setIcon(icon);
        }

        if (player2.getUsername().equalsIgnoreCase(username)) {
            turn = 2;
            inGameView.getLbAvatar1().setBorder(javax.swing.BorderFactory.createTitledBorder("Chờ"));
            inGameView.getLbAvatar2().setBorder(javax.swing.BorderFactory.createTitledBorder("Đang đánh.."));
            Icon icon = getIconVs("icons8_sword_48px_1.png");
            inGameView.getLbVersus().setIcon(icon);
        }
    }

    public void changeTurnFrom(String username) {
        if (username.equalsIgnoreCase(player1.getUsername())) {
            setTurn(player2.getUsername());
        } else {
            setTurn(player1.getUsername());
        }
    }

    public void addChat(ChatItem c) {
        inGameView.getjTextArea1().append(c.toString() + "\n");
    }

    class InGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == inGameView.getjButton2()) {
                ClientMain.socketHandler.sendMessage.gameStart();
            } else if (e.getSource() == inGameView.getjButton1()) {
                String message = inGameView.getjTextField1().getText();
                ClientMain.socketHandler.sendMessage.chatRoom(message);
                inGameView.getjTextField1().setText("");

            } else if (e.getSource() == inGameView.getjButton3()) {
                int check = JOptionPane.showConfirmDialog(ClientMain.inGameView, "Bạn có muốn thoát phòng", "Thoát phòng", JOptionPane.YES_NO_OPTION);
                if (check == 1) {
                    return;
                }
                ClientMain.socketHandler.sendMessage.leaveRoom();
            } else {
                String s = e.getActionCommand();
                System.out.println(s);
                int k = s.indexOf(32);
                int i = Integer.parseInt(s.substring(0, k));
                int j = Integer.parseInt(s.substring(k + 1, s.length()));

                for (int c = 0; c < 16; c++) {
                    for (int d = 0; d < 16; d++) {
                        System.out.print(tick[c][d] + " ");
                    }
                    System.out.println();
                }

                if (tick[i][j].equalsIgnoreCase("...")) {
                    addPoint(i, j, ClientMain.socketHandler.readMessage.getPlayerLogin().getUsername());
                }

            }

        }

    }

    public void disableBtn() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                inGameView.getBtnOnBoard()[i][j].setEnabled(false);
            }
        }
    }

    public String[][] getTick() {
        return tick;
    }

    public void setTick(String[][] tick) {
        this.tick = tick;
    }

    public TimeCount getTimeCount() {
        return timeCount;
    }

    public void startTime() {
        giay = 0;
        phut = 0;

        if (!flagTime) {
            timeCount.start();
            flagTime = true;
        } else {
            timeCount.resume();
        }
    }

    public void stopTime() {
        timeCount.suspend();
    }

    class TimeCount extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                giay += 1;
                if (giay == 60) {
                    giay = 0;
                    phut += 1;
                }
                if (phut >= 10 && giay >= 10) {
                    inGameView.getjLabel3().setText(phut + " : " + giay);
                } else if (phut >= 10 && giay < 10) {
                    inGameView.getjLabel3().setText(phut + " : 0" + giay);
                } else if (phut < 10 && giay >= 10) {
                    inGameView.getjLabel3().setText("0" + phut + " : " + giay);
                } else if (phut < 10 && giay < 10) {
                    inGameView.getjLabel3().setText("0" + phut + " : 0" + giay);
                }
            }
        }

    }

}
