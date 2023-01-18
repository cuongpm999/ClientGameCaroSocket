/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ptit.controller.sockethandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import vn.ptit.ClientMain;
import vn.ptit.model.FriendInvite;
import vn.ptit.model.Move;
import vn.ptit.model.ObjectWrapper;
import vn.ptit.model.Player;
import vn.ptit.model.Room;
import vn.ptit.utils.StreamData;

/**
 *
 * @author Cuong Pham
 */
public class SendMessage extends Thread {

    private ObjectOutputStream objectOutputStream;

    public SendMessage(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    public void signup(Player player) {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.SIGNUP.name(), player);
        sendData(objectWrapper);
    }

    public void sendData(ObjectWrapper objectWrapper) {
        try {
            objectOutputStream.writeObject(objectWrapper);
            objectOutputStream.flush();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void login(Player player) {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.LOGIN.name(), player);
        sendData(objectWrapper);
    }

    public void listOnline() {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.LIST_ONLINE.name(), null);
        sendData(objectWrapper);
    }

    public void createRoom() {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.CREATE_ROOM.name(), null);
        sendData(objectWrapper);
    }

    public void listRoom() {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.LIST_ROOM.name(), null);
        sendData(objectWrapper);
    }

    public void joinRoom(String id) {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.JOIN_ROOM.name(), id);
        sendData(objectWrapper);
    }

    public void sendInviteFriend(String username) {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.SEND_INVITE_FRIEND.name(), username);
        sendData(objectWrapper);
    }

    public void sendAcceptInviteFriend(FriendInvite friendInvite) {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.ACCEPT_INVITE_FRIEND.name(), friendInvite);
        sendData(objectWrapper);
    }

    public void listFriend() {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.LIST_FRIEND.name(), null);
        sendData(objectWrapper);
    }

    public void checkFriend() {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.CHECK_FRIEND.name(), null);
        sendData(objectWrapper);
    }

    public void sendInviteRoom(String username) {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.SEND_INVITE_ROOM.name(), username);
        sendData(objectWrapper);
    }

    public void chatRoom(String chatMsg) {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.CHAT_ROOM.name(), chatMsg);
        sendData(objectWrapper);
    }
    
    public void gameStart() {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.START.name(), null);
        sendData(objectWrapper);
    }
    
    public void move(Move move) {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.MOVE.name(), move);
        sendData(objectWrapper);
    }
    
    public void leaveRoom() {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.LEAVE_ROOM.name(), null);
        sendData(objectWrapper);
    }
    
    public void stat() {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.PLAYER_STAT.name(), null);
        sendData(objectWrapper);
    }
    
    public void logout() {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.LOGOUT.name(), null);
        sendData(objectWrapper);
    }
    
    public void update(Player player) {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.UPDATE_PROFILE.name(), player);
        sendData(objectWrapper);
    }
    
    public void findMatch(){
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.FIND_MATCH.name(), null);
        sendData(objectWrapper);
    }
    
    public void acceptFindMatch(String idRand) {
        ObjectWrapper objectWrapper = new ObjectWrapper(StreamData.Type.ACCEPT_FIND_MATCH.name(), idRand);
        sendData(objectWrapper);
    }

}
