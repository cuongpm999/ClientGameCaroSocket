/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ptit.controller.viewhandler;

import vn.ptit.controller.sockethandler.SocketHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import vn.ptit.view.scene.ConnectServerView;
import vn.ptit.view.scene.SignupView;
import vn.ptit.ClientMain;

/**
 *
 * @author Cuong Pham
 */
public class ConnectServerController {

    private ConnectServerView connectServerView;

    public ConnectServerController(ConnectServerView connectServerView) {
        this.connectServerView = connectServerView;
        connectServerView.addListener(new ConnectServerListener());
    }

    class ConnectServerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String addr = connectServerView.getjTextField1().getText();
            int port = Integer.parseInt(connectServerView.getjTextField2().getText());
            ClientMain.socketHandler.connect(addr, port);
        }

    }
}
