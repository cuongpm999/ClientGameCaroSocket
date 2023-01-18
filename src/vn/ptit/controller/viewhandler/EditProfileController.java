/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ptit.controller.viewhandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import vn.ptit.ClientMain;
import vn.ptit.model.Player;
import vn.ptit.utils.Avatar;
import vn.ptit.view.scene.EditProfileView;
import vn.ptit.view.scene.SignupView;

/**
 *
 * @author Cuong Pham
 */
public class EditProfileController {

    private EditProfileView editProfileView;

    public EditProfileController(EditProfileView editProfileView) {
        this.editProfileView = editProfileView;
        this.editProfileView.addListener(new EditProfileListener());
        editProfileView.getjTextField1().setText(ClientMain.socketHandler.readMessage.getPlayerLogin().getUsername());
        editProfileView.getjTextField2().setText(ClientMain.socketHandler.readMessage.getPlayerLogin().getPassword());
        editProfileView.getjTextField3().setText(ClientMain.socketHandler.readMessage.getPlayerLogin().getFullName());

        for (int i = 0; i < Avatar.LIST.length; i++) {
            if (Avatar.LIST[i].equalsIgnoreCase(ClientMain.socketHandler.readMessage.getPlayerLogin().getImg())) {
                editProfileView.getCbAvatar().setSelectedIndex(i);
                break;
            }
        }

    }

    class EditProfileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == editProfileView.getjButton1()) {
                String password = editProfileView.getjTextField2().getText();
                String fullname = editProfileView.getjTextField3().getText();
                String img = editProfileView.getAvatar();
                Player player =  ClientMain.socketHandler.readMessage.getPlayerLogin();
                player.setPassword(password);
                player.setFullName(fullname);
                player.setImg(img);
                ClientMain.socketHandler.sendMessage.update(player);
            }

            if (e.getSource() == editProfileView.getjButton2()) {
                ClientMain.openScene(ClientMain.SceneName.MAINMENU);
                ClientMain.closeScene(ClientMain.SceneName.EDIT);
                ClientMain.socketHandler.sendMessage.listOnline();
                ClientMain.socketHandler.sendMessage.listRoom();
                ClientMain.socketHandler.sendMessage.checkFriend();
            }
        }

    }

}
