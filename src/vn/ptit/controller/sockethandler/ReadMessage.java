/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ptit.controller.sockethandler;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import vn.ptit.ClientMain;
import vn.ptit.model.ChatItem;
import vn.ptit.model.Friend;
import vn.ptit.model.FriendInvite;
import vn.ptit.model.Match;
import vn.ptit.model.ObjectWrapper;
import vn.ptit.model.Player;
import vn.ptit.model.PlayerInMatch;
import vn.ptit.model.PlayerStat;
import vn.ptit.model.Room;
import vn.ptit.model.RoomInvite;
import vn.ptit.utils.Avatar;
import vn.ptit.utils.StreamData;

/**
 *
 * @author Cuong Pham
 */
public class ReadMessage extends Thread {

    private ObjectInputStream objectInputStream;
    private Player playerLogin = null;
    private List<PlayerStat> playerStats;

    public ReadMessage(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    public Player getPlayerLogin() {
        return playerLogin;
    }

    public List<PlayerStat> getPlayerStats() {
        return playerStats;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ObjectWrapper objectWrapper = new ObjectWrapper();
                objectWrapper = (ObjectWrapper) objectInputStream.readObject();
                String received = objectWrapper.getIdentifier();
                StreamData.Type type = StreamData.getTypeFromData(received);
                switch (type) {
                    case CONNECT_SERVER:
                        onConnectServer();
                        break;
                    case SIGNUP:
                        onReceiveSignup(objectWrapper);
                        break;
                    case LOGIN:
                        onReceiveLogin(objectWrapper);
                        break;
                    case CHAT_ROOM:
                        onReceiveChatRoom(objectWrapper);
                        break;
                    case LIST_ONLINE:
                        onReceiveListOnline(objectWrapper);
                        break;
                    case CREATE_ROOM:
                        onReceiveCreateRoom(objectWrapper);
                        break;
                    case LIST_ROOM:
                        onReceiveListRoom(objectWrapper);
                        break;
                    case JOIN_ROOM:
                        onReceiveJoinRoom(objectWrapper);
                        break;
                    case SEND_INVITE_FRIEND:
                        onReceiveSendInviteFriend(objectWrapper);
                        break;
                    case ACCEPT_INVITE_FRIEND:
                        onReceiveAcceptInviteFriend(objectWrapper);
                        break;
                    case LIST_FRIEND:
                        onReceiveListFriend(objectWrapper);
                        break;
                    case CHECK_FRIEND:
                        onReceiveCheckFriend(objectWrapper);
                        break;
                    case SEND_INVITE_ROOM:
                        onReceiveSendInviteRoom(objectWrapper);
                        break;
                    case START:
                        onReceiveGameStart(objectWrapper);
                        break;
                    case MOVE:
                        onReceiveMove(objectWrapper);
                        break;
                    case LEAVE_ROOM:
                        onReceiveLeaveRoom(objectWrapper);
                        break;
                    case PLAYER_STAT:
                        onReceiveStat(objectWrapper);
                        break;
                    case LOGOUT:
                        onReceiveLogout(objectWrapper);
                        break;
                    case UPDATE_PROFILE:
                        onReceiveUpdateProfile(objectWrapper);
                        break;
                    case FIND_MATCH:
                        onReceiveFindMatch(objectWrapper);
                        break;
                    case ACCEPT_FIND_MATCH:
                        onReceiveAcceptFindMatch(objectWrapper);
                        break;
                    case TIME_FIND_MATCH:
                        onReceiveTimeFindMatch(objectWrapper);
                        break;
                }
            } catch (IOException ex) {
                System.out.println(ex);
            } catch (ClassNotFoundException ex) {
                System.out.println(ex);
            }
        }

    }

    private void onConnectServer() {
        ClientMain.closeScene(ClientMain.SceneName.CONNECTSERVER);
        ClientMain.openScene(ClientMain.SceneName.LOGIN);
    }

    private void onReceiveSignup(ObjectWrapper objectWrapper) {
        // get status from data
        String[] splitted = objectWrapper.getIdentifier().split(";");
        String status = splitted[1];

        if (status.equals("falied")) {
            JOptionPane.showMessageDialog(ClientMain.signupView, "Đăng kí thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);

        } else if (status.equals("success")) {
            JOptionPane.showMessageDialog(ClientMain.signupView, "Đăng ký thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            ClientMain.closeScene(ClientMain.SceneName.SIGNUP);
            ClientMain.openScene(ClientMain.SceneName.LOGIN);
        }
    }

    private void onReceiveLogin(ObjectWrapper objectWrapper) {
        // get status from data
        String[] splitted = objectWrapper.getIdentifier().split(";");
        String status = splitted[1];

        if (status.equals("falied")) {
            JOptionPane.showMessageDialog(ClientMain.loginView, "Đăng nhập thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);

        } else if (status.equals("success")) {
            this.playerLogin = (Player) objectWrapper.getObject();
            System.out.println(playerLogin.getFullName());
            JOptionPane.showMessageDialog(ClientMain.signupView, "Đăng nhập thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            ClientMain.openScene(ClientMain.SceneName.MAINMENU);
            ClientMain.closeScene(ClientMain.SceneName.LOGIN);
            ClientMain.socketHandler.sendMessage.listOnline();
            ClientMain.socketHandler.sendMessage.listRoom();
            ClientMain.socketHandler.sendMessage.checkFriend();
        }
    }

    private void onReceiveChatRoom(ObjectWrapper objectWrapper) {
        ChatItem c = (ChatItem) objectWrapper.getObject();
        if (ClientMain.inGameController != null) {
            ClientMain.inGameController.addChat(c);
        }
        if (ClientMain.roomController != null) {
            ClientMain.roomController.addChat(c);
        }
    }

    private void onReceiveListOnline(ObjectWrapper objectWrapper) {
        List<Player> players = new ArrayList<>();
        players = (List<Player>) objectWrapper.getObject();
        if (ClientMain.mainMenuController != null) {
            if (ClientMain.mainMenuView != null && ClientMain.mainMenuView.isDisplayable()) {
                ClientMain.mainMenuController.setListOnline(players);
            }
        }
    }

    private void onReceiveCreateRoom(ObjectWrapper objectWrapper) {
        ClientMain.openScene(ClientMain.SceneName.ROOM);
        Room room = (Room) objectWrapper.getObject();
        ClientMain.roomController.setPlayer1(room);
        ClientMain.closeScene(ClientMain.SceneName.MAINMENU);
        ClientMain.socketHandler.sendMessage.listRoom();
        ClientMain.socketHandler.sendMessage.listOnline();
        ClientMain.socketHandler.sendMessage.checkFriend();

    }

    private void onReceiveListRoom(ObjectWrapper objectWrapper) {
        List<Room> rooms = new ArrayList<>();
        rooms = (List<Room>) objectWrapper.getObject();
        if (ClientMain.mainMenuController != null) {
            if (ClientMain.mainMenuView != null && ClientMain.mainMenuView.isDisplayable()) {
                ClientMain.mainMenuController.setListRoom(rooms);
            }
        }
    }

    private void onReceiveJoinRoom(ObjectWrapper objectWrapper) {
        String[] splitted = objectWrapper.getIdentifier().split(";");
        String status = splitted[1];
        if (status.equals("success")) {
            Room room = (Room) objectWrapper.getObject();
            if (!room.getCreateBy().getUsername().equalsIgnoreCase(playerLogin.getUsername())) {
                ClientMain.closeScene(ClientMain.SceneName.MAINMENU);
                ClientMain.openScene(ClientMain.SceneName.ROOM);
            }
            System.out.println("Size " + room.getPlayers().size() + " " + room.getStatus());
            ClientMain.roomController.setPlayer1(room);
            ClientMain.roomController.setPlayer2(room);
            if (room.getCreateBy().getUsername().equalsIgnoreCase(this.playerLogin.getUsername())) {
                ClientMain.roomView.getjButton2().setEnabled(true);
            }
            ClientMain.socketHandler.sendMessage.listOnline();
            ClientMain.socketHandler.sendMessage.listRoom();
            ClientMain.socketHandler.sendMessage.checkFriend();

        } else if (status.equals("falied")) {
            JOptionPane.showMessageDialog(ClientMain.mainMenuView, "Phòng đã đầy", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onReceiveGameStart(ObjectWrapper objectWrapper) {
        if (ClientMain.roomView != null && ClientMain.roomView.isDisplayable()) {
            ClientMain.closeScene(ClientMain.SceneName.ROOM);
        }
        if (ClientMain.mainMenuView != null && ClientMain.mainMenuView.isDisplayable()) {
            ClientMain.closeScene(ClientMain.SceneName.MAINMENU);
        }
        if (ClientMain.inGameView == null || !ClientMain.inGameView.isDisplayable()) {
            ClientMain.openScene(ClientMain.SceneName.INGAME);
        }

        Match match = (Match) objectWrapper.getObject();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                ClientMain.inGameView.getBtnOnBoard()[i][j].setEnabled(true);
            }
        }

        System.out.println("Test match " + match.getRoom().getPlayers().size());
        ClientMain.inGameController.setTick(match.getBoard().getPieces());
        ClientMain.inGameController.reset();
        ClientMain.inGameController.startTime();
        ClientMain.inGameController.setPlayer1(match.getRoom());
        ClientMain.inGameController.setPlayer2(match.getRoom());
    }

    private void onReceiveMove(ObjectWrapper objectWrapper) {
        Match match = (Match) objectWrapper.getObject();
        ClientMain.inGameController.setTick(match.getBoard().getPieces());
        for (PlayerInMatch playerInMatch : match.getPlayerInMatchs()) {
            if (playerInMatch.getMove() != null) {
                if (playerInMatch.getResult().equalsIgnoreCase("THẮNG")) {
                    ClientMain.inGameController.stopTime();
                    ClientMain.inGameController.disableBtn();
                    ClientMain.inGameController.addPointToCompetitor(playerInMatch.getMove().getX(), playerInMatch.getMove().getY(), playerInMatch.getPlayer().getUsername());
                    JOptionPane.showMessageDialog(ClientMain.inGameView, playerInMatch.getPlayer().getUsername() + " THẮNG", "THẮNG", JOptionPane.INFORMATION_MESSAGE);
                    if (match.getRoom().getCreateBy().getUsername().equalsIgnoreCase(this.playerLogin.getUsername())) {
                        ClientMain.inGameView.getjButton2().setEnabled(true);
                    }
                    ClientMain.inGameView.getjButton3().setEnabled(true);
                } else if (playerInMatch.getResult().equalsIgnoreCase("HÒA")) {
                    ClientMain.inGameController.stopTime();
                    ClientMain.inGameController.disableBtn();
                    ClientMain.inGameController.addPointToCompetitor(playerInMatch.getMove().getX(), playerInMatch.getMove().getY(), playerInMatch.getPlayer().getUsername());
                    JOptionPane.showMessageDialog(ClientMain.inGameView, "HÒA", "HÒA", JOptionPane.INFORMATION_MESSAGE);
                    if (match.getRoom().getCreateBy().getUsername().equalsIgnoreCase(this.playerLogin.getUsername())) {
                        ClientMain.inGameView.getjButton2().setEnabled(true);
                    }
                    ClientMain.inGameView.getjButton3().setEnabled(true);
                } else {
                    ClientMain.inGameController.addPointToCompetitor(playerInMatch.getMove().getX(), playerInMatch.getMove().getY(), playerInMatch.getPlayer().getUsername());
                    ClientMain.inGameController.changeTurnFrom(playerInMatch.getPlayer().getUsername());
                }
                break;
            }
        }

    }

    private void onReceiveStat(ObjectWrapper objectWrapper) {
        playerStats = new ArrayList<>();
        playerStats = (List<PlayerStat>) objectWrapper.getObject();
        ClientMain.openScene(ClientMain.SceneName.STAT);
        ClientMain.closeScene(ClientMain.SceneName.MAINMENU);

    }

    private void onReceiveLogout(ObjectWrapper objectWrapper) {
        this.playerLogin = null;
        ClientMain.openScene(ClientMain.SceneName.LOGIN);
        ClientMain.closeScene(ClientMain.SceneName.MAINMENU);
        ClientMain.socketHandler.sendMessage.listOnline();
        ClientMain.socketHandler.sendMessage.listRoom();
        ClientMain.socketHandler.sendMessage.checkFriend();

    }

    private void onReceiveUpdateProfile(ObjectWrapper objectWrapper) {
        String[] splitted = objectWrapper.getIdentifier().split(";");
        String status = splitted[1];

        if (status.equals("falied")) {
            JOptionPane.showMessageDialog(ClientMain.loginView, "Cập nhật thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);

        } else if (status.equals("success")) {
            this.playerLogin = (Player) objectWrapper.getObject();
            System.out.println(playerLogin.getFullName());
            JOptionPane.showMessageDialog(ClientMain.signupView, "Cập nhật thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            ClientMain.openScene(ClientMain.SceneName.MAINMENU);
            ClientMain.closeScene(ClientMain.SceneName.EDIT);
            ClientMain.socketHandler.sendMessage.listOnline();
            ClientMain.socketHandler.sendMessage.listRoom();
            ClientMain.socketHandler.sendMessage.checkFriend();
        }
    }

    private void onReceiveSendInviteFriend(ObjectWrapper objectWrapper) {
        Player player = (Player) objectWrapper.getObject();
        int check = JOptionPane.showConfirmDialog(ClientMain.mainMenuView, player.getUsername() + " gửi lời mời kết bạn", "Lời mời kết bạn", JOptionPane.YES_NO_OPTION);
        if (check == 1) {
            FriendInvite friendInvite = new FriendInvite(player, null, false);
            ClientMain.socketHandler.sendMessage.sendAcceptInviteFriend(friendInvite);
        }
        if (check == 0) {
            FriendInvite friendInvite = new FriendInvite(player, null, true);
            ClientMain.socketHandler.sendMessage.sendAcceptInviteFriend(friendInvite);
        }
    }

    private void onReceiveAcceptInviteFriend(ObjectWrapper objectWrapper) {
        String[] splitted = objectWrapper.getIdentifier().split(";");
        String status = splitted[1];
        Player player = (Player) objectWrapper.getObject();
        if (status.equals("falied")) {
            JOptionPane.showMessageDialog(ClientMain.inGameView, player.getUsername() + " không chấp nhận lời mời kết bạn");

        } else if (status.equals("success")) {
            ClientMain.socketHandler.sendMessage.listOnline();
            ClientMain.socketHandler.sendMessage.listRoom();
            ClientMain.socketHandler.sendMessage.checkFriend();
            JOptionPane.showMessageDialog(ClientMain.inGameView, player.getUsername() + " chấp nhận lời mời kết bạn");
        }
    }

    private void onReceiveListFriend(ObjectWrapper objectWrapper) {
        List<Friend> friends = (List<Friend>) objectWrapper.getObject();
        ClientMain.mainMenuController.setFriends(friends);
        if (ClientMain.mainMenuController != null) {
            if (ClientMain.mainMenuView != null && ClientMain.mainMenuView.isDisplayable()) {
                ClientMain.mainMenuController.setListFriend(friends);
            }
        }
        if (ClientMain.roomController != null) {
            if (ClientMain.roomView != null && ClientMain.roomView.isDisplayable()) {
                ClientMain.roomController.setListFriend(friends);
            }
        }
    }

    private void onReceiveCheckFriend(ObjectWrapper objectWrapper) {
        ClientMain.socketHandler.sendMessage.listFriend();
    }

    private void onReceiveSendInviteRoom(ObjectWrapper objectWrapper) {
        RoomInvite roomInvite = (RoomInvite) objectWrapper.getObject();
        int check = JOptionPane.showConfirmDialog(ClientMain.roomView, roomInvite.getUsername() + " muốn thách đấu bạn phòng " + roomInvite.getId(), "Lời mời thách đấu", JOptionPane.YES_NO_OPTION);
        if (check == 0) {
            ClientMain.socketHandler.sendMessage.joinRoom(roomInvite.getId());
        }
    }

    private void onReceiveLeaveRoom(ObjectWrapper objectWrapper) {
        Room room = (Room) objectWrapper.getObject();
        if (room != null) {
            if (this.playerLogin.getUsername().equalsIgnoreCase(room.getCreateBy().getUsername())) {
                if (ClientMain.inGameView != null && ClientMain.inGameView.isDisplayable()) {
                    ClientMain.closeScene(ClientMain.SceneName.INGAME);
                    ClientMain.openScene(ClientMain.SceneName.ROOM);
                }
                ClientMain.roomView.getjButton2().setEnabled(false);
                ClientMain.roomController.setPlayer1(room);
                ClientMain.roomController.setPlayer2Null();
            }

        } else {
            if (ClientMain.inGameView != null && ClientMain.inGameView.isDisplayable()) {
                ClientMain.closeScene(ClientMain.SceneName.INGAME);
                ClientMain.openScene(ClientMain.SceneName.MAINMENU);
            }
            if (ClientMain.roomView != null && ClientMain.roomView.isDisplayable()) {
                ClientMain.closeScene(ClientMain.SceneName.ROOM);
                ClientMain.openScene(ClientMain.SceneName.MAINMENU);
            }
            ClientMain.socketHandler.sendMessage.listOnline();
            ClientMain.socketHandler.sendMessage.listRoom();
            ClientMain.socketHandler.sendMessage.checkFriend();

        }

    }

    private void onReceiveFindMatch(ObjectWrapper objectWrapper) {
        String idRand = (String) objectWrapper.getObject();
        JOptionPane.showMessageDialog(ClientMain.mainMenuView, "Đã tìm thấy trận");
        ClientMain.socketHandler.sendMessage.acceptFindMatch(idRand);
    }

    private void onReceiveAcceptFindMatch(ObjectWrapper objectWrapper) {
        Room room = (Room) objectWrapper.getObject();
        if (room.getCreateBy().getUsername().equalsIgnoreCase(this.playerLogin.getUsername())) {
            System.out.println("Check joint match...");
            ClientMain.socketHandler.sendMessage.gameStart();
        }

    }

    private void onReceiveTimeFindMatch(ObjectWrapper objectWrapper) {
        int cnt = (int) objectWrapper.getObject();
        ClientMain.mainMenuView.getjLabel4().setText(cnt + " s");
        if (cnt == 0) {
            ClientMain.mainMenuView.getjPanel5().setVisible(false);
            ClientMain.mainMenuController.setBtnDisable(false);
        }
    }
}
