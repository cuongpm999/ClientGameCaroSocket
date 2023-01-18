/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ptit.controller.viewhandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import vn.ptit.ClientMain;
import vn.ptit.model.Friend;
import vn.ptit.model.Player;
import vn.ptit.model.Room;
import vn.ptit.view.scene.MainMenuView;

/**
 *
 * @author Cuong Pham
 */
public class MainMenuController {

    private MainMenuView mainMenuView;
    private List<Friend> friends;

    public MainMenuController(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.friends = new ArrayList<>();
        this.mainMenuView.addListener(new MainMenuListener());
        mainMenuView.getjTable2().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int r = mainMenuView.getjTable2().getSelectedRow();
                String username = mainMenuView.getjTable2().getValueAt(r, 1).toString();
                if (username.equalsIgnoreCase(ClientMain.socketHandler.readMessage.getPlayerLogin().getUsername())) {
                    return;
                }
                if (!mainMenuView.getjTable2().getValueAt(r, 2).toString().equalsIgnoreCase("Trực tuyến")) {
                    return;
                }
                for (Friend friend : friends) {
                    if (username.equalsIgnoreCase(friend.getFriendPlayer().getUsername())) {
                        return;
                    }
                }
                ClientMain.socketHandler.sendMessage.sendInviteFriend(username);
            }

        });
        mainMenuView.getjLabel1().setText("Hello, " + ClientMain.socketHandler.readMessage.getPlayerLogin().getFullName());
    }

    public void setListOnline(List<Player> players) {
        DefaultTableModel modelOnline = (DefaultTableModel) mainMenuView.getjTable2().getModel();
        modelOnline.setRowCount(0);
        for (Player player : players) {
            modelOnline.addRow(player.toObject());
        }
    }

    public void setListFriend(List<Friend> friends) {
        DefaultTableModel modelFriend = (DefaultTableModel) mainMenuView.getjTable3().getModel();
        modelFriend.setRowCount(0);
        for (Friend friend : friends) {
            modelFriend.addRow(friend.toObject());
        }
    }

    public void setListRoom(List<Room> rooms) {
        DefaultTableModel modelRoom = (DefaultTableModel) mainMenuView.getjTable1().getModel();
        modelRoom.setRowCount(0);
        for (Room room : rooms) {
            modelRoom.addRow(room.toObject());
        }
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    class MainMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (mainMenuView.getjButton2() == e.getSource()) {
                ClientMain.socketHandler.sendMessage.createRoom();
            }

            if (mainMenuView.getjButton1() == e.getSource()) {
                ClientMain.socketHandler.sendMessage.stat();
            }

            if (mainMenuView.getjButton4() == e.getSource()) {
                ClientMain.socketHandler.sendMessage.logout();
            }

            if (mainMenuView.getjButton3() == e.getSource()) {
                ClientMain.openScene(ClientMain.SceneName.EDIT);
                ClientMain.closeScene(ClientMain.SceneName.MAINMENU);
            }

            if (mainMenuView.getjButton5() == e.getSource()) {
                ClientMain.socketHandler.sendMessage.findMatch();
                mainMenuView.getjPanel5().setVisible(true);
                setBtnDisable(true);
            }

        }

    }

    public void setBtnDisable(boolean flag) {
        if (flag) {
            mainMenuView.getjButton1().setEnabled(false);
            mainMenuView.getjButton2().setEnabled(false);
            mainMenuView.getjButton3().setEnabled(false);
            mainMenuView.getjButton4().setEnabled(false);
            mainMenuView.getjButton5().setEnabled(false);
        } else {
            mainMenuView.getjButton1().setEnabled(true);
            mainMenuView.getjButton2().setEnabled(true);
            mainMenuView.getjButton3().setEnabled(true);
            mainMenuView.getjButton4().setEnabled(true);
            mainMenuView.getjButton5().setEnabled(true);
        }
    }

}
