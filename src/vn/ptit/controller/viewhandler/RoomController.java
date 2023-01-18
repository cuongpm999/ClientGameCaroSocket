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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import vn.ptit.ClientMain;
import vn.ptit.model.ChatItem;
import vn.ptit.model.Friend;
import vn.ptit.model.Player;
import vn.ptit.model.Room;
import vn.ptit.utils.Avatar;
import vn.ptit.view.scene.InGameView;
import vn.ptit.view.scene.RoomView;

/**
 *
 * @author Cuong Pham
 */
public class RoomController {

    private RoomView roomView;
    
    public RoomController(RoomView roomView) {
        this.roomView = roomView;
        this.roomView.addListener(new RoomListener()); 
        disableBtn();
        roomView.getjLabel4().setText("Người chơi: " + ClientMain.socketHandler.readMessage.getPlayerLogin().getUsername());
        roomView.getjTable1().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int r = roomView.getjTable1().getSelectedRow();
                String username = roomView.getjTable1().getValueAt(r, 1).toString();
                String status = roomView.getjTable1().getValueAt(r, 2).toString();
                if(!status.equalsIgnoreCase("Trực tuyến")) return;
                ClientMain.socketHandler.sendMessage.sendInviteRoom(username);
            }
            
        });
    }
    
    public void setListFriend(List<Friend> friends) {
        DefaultTableModel modelFriend = (DefaultTableModel) roomView.getjTable1().getModel();
        modelFriend.setRowCount(0);
        for (Friend friend : friends) {
            modelFriend.addRow(friend.toObject());
        }
    }
    
    public void setPlayer1(Room room) {
        String username = room.getPlayers().get(0).getUsername();
        roomView.getLbPlayerNameId1().setText(username);
        roomView.getLbAvatar1().setIcon(new ImageIcon(Avatar.PATH + room.getPlayers().get(0).getImg()));
    }
    
    public void setPlayer2(Room room) {
        String username = room.getPlayers().get(1).getUsername();
        roomView.getLbPlayerNameId2().setText(username);
        roomView.getLbAvatar2().setIcon(new ImageIcon(Avatar.PATH + room.getPlayers().get(1).getImg()));
    }
    
    public void setPlayer2Null() {
        roomView.getLbPlayerNameId2().setText("Đang chờ...");
        roomView.getLbAvatar2().setIcon(new ImageIcon("src/vn/ptit/view/img/icons8-question-mark-96.png"));
    }

    public void addChat(ChatItem c) {
        roomView.getjTextArea1().append(c.toString() + "\n");
    }
    
    class RoomListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == roomView.getjButton2()) {
                ClientMain.socketHandler.sendMessage.gameStart();
            } 
            if(e.getSource() == roomView.getjButton1()){
                String message = roomView.getjTextField1().getText();
                ClientMain.socketHandler.sendMessage.chatRoom(message);
                roomView.getjTextField1().setText("");

            }
            if (e.getSource() == roomView.getjButton3()) {
                int check = JOptionPane.showConfirmDialog(ClientMain.roomView, "Bạn có muốn thoát phòng", "Thoát phòng", JOptionPane.YES_NO_OPTION);
                if (check == 1) {
                    return;
                }
                ClientMain.socketHandler.sendMessage.leaveRoom();
            }

        }
        
    }
    
    public void disableBtn() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                roomView.getBtnOnBoard()[i][j].setEnabled(false);
            }
        }
    }  
}
