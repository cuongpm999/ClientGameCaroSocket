/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ptit.controller.viewhandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import vn.ptit.ClientMain;
import vn.ptit.model.Player;
import vn.ptit.view.scene.LoginView;

/**
 *
 * @author Cuong Pham
 */
public class LoginController {
    private LoginView loginView;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.loginView.addListener(new LoginListener());
    }
    
    class LoginListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == loginView.getjButton1()){
                String username = loginView.getjTextField1().getText();
                String password = loginView.getjPasswordField1().getText();
                
                Player player = new Player();
                player.setUsername(username);
                player.setPassword(password);
                
                ClientMain.socketHandler.sendMessage.login(player);
            }
            
            if(e.getSource() == loginView.getjButton2()){
                ClientMain.openScene(ClientMain.SceneName.SIGNUP);
                ClientMain.closeScene(ClientMain.SceneName.LOGIN);
            }
        }
        
    }
}
