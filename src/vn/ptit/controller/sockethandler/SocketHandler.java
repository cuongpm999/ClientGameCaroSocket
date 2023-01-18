/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ptit.controller.sockethandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Cuong Pham
 */
public class SocketHandler {

    public Socket socket;
    public ObjectInputStream objectInputStream;
    public ObjectOutputStream objectOutputStream;
    
    public SendMessage sendMessage;
    public ReadMessage readMessage;

    public boolean connect(String addr, int port) {
        try {
            socket = new Socket(addr, port);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            
            sendMessage = new SendMessage(objectOutputStream);
            sendMessage.start();
            
            readMessage = new ReadMessage(objectInputStream);
            readMessage.start();
            
            return true;
        } catch (IOException e) {
            return false;
        }

    }

}
