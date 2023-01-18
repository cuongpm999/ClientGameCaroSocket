/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ptit.controller.viewhandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vn.ptit.ClientMain;
import vn.ptit.model.Player;
import vn.ptit.view.scene.SignupView;

/**
 *
 * @author Cuong Pham
 */
public class SignupController {
    private SignupView signupView;

    public SignupController(SignupView signupView) {
        this.signupView = signupView;
        this.signupView.addListener(new SignupListener());
    }
    
    class SignupListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == signupView.getjButton1()){
                String username = signupView.getjTextField1().getText();
                String password = signupView.getjTextField2().getText();
                String fullName = signupView.getjTextField3().getText();
                String img = signupView.getAvatar();
                Player player = new Player(img, username, password, fullName, null);
                ClientMain.socketHandler.sendMessage.signup(player);
            }
            
            if(e.getSource() == signupView.getjButton2()){
                ClientMain.openScene(ClientMain.SceneName.LOGIN);
                ClientMain.closeScene(ClientMain.SceneName.SIGNUP);
            }
        }

    }
    
}
