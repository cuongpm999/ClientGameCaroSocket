/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ptit;

import vn.ptit.controller.sockethandler.SocketHandler;
import vn.ptit.controller.viewhandler.ConnectServerController;
import vn.ptit.controller.viewhandler.InGameController;
import vn.ptit.controller.viewhandler.RoomController;
import vn.ptit.controller.viewhandler.LoginController;
import vn.ptit.controller.viewhandler.EditProfileController;
import vn.ptit.controller.viewhandler.MainMenuController;
import vn.ptit.controller.viewhandler.PlayerStatController;
import vn.ptit.controller.viewhandler.SignupController;
import vn.ptit.view.scene.ConnectServerView;
import vn.ptit.view.scene.InGameView;
import vn.ptit.view.scene.LoginView;
import vn.ptit.view.scene.MainMenuView;
import vn.ptit.view.scene.PlayerStatView;
import vn.ptit.view.scene.RoomView;
import vn.ptit.view.scene.SignupView;
import vn.ptit.view.scene.EditProfileView;

/**
 *
 * @author Cuong Pham
 */
public class ClientMain {
    public static SocketHandler socketHandler;
    public static ConnectServerView connectServerView;
    public static SignupView signupView;
    public static LoginView loginView;
    public static MainMenuController mainMenuController;
    public static MainMenuView mainMenuView;
    public static InGameView inGameView;
    public static InGameController inGameController;
    public static RoomView roomView;
    public static RoomController roomController;
    public static PlayerStatView playerStatView;
    public static EditProfileView editProfileView;
    
    public enum SceneName {
        CONNECTSERVER,
        SIGNUP,
        LOGIN,
        MAINMENU,
        ROOM,
        INGAME,
        STAT,
        EDIT,
    }

    public ClientMain() {
        socketHandler = new SocketHandler();
        openScene(SceneName.CONNECTSERVER);
    }

    public static void openScene(SceneName sceneName) {
        switch (sceneName) {
            case CONNECTSERVER:
                connectServerView = new ConnectServerView();
                ConnectServerController connectServerController = new ConnectServerController(connectServerView);
                connectServerView.setVisible(true);
                break;
            case SIGNUP:
                signupView = new SignupView();
                SignupController signupController = new SignupController(signupView);
                signupView.setVisible(true);
                break;
            case LOGIN:
                loginView = new LoginView();
                LoginController loginController = new LoginController(loginView);
                loginView.setVisible(true);
                break;
            case MAINMENU:
                mainMenuView = new MainMenuView();
                mainMenuController = new MainMenuController(mainMenuView);
                mainMenuView.setVisible(true);
                break;
            case ROOM:
                roomView = new RoomView();
                roomController = new RoomController(roomView);
                roomView.setVisible(true);
                break;
            case INGAME:
                inGameView = new InGameView();
                inGameController = new InGameController(inGameView);
                inGameView.setVisible(true);
                break;
            case STAT:
                playerStatView = new PlayerStatView();
                PlayerStatController playerStatController = new PlayerStatController(playerStatView);
                playerStatView.setVisible(true);
                break;
            case EDIT:
                editProfileView = new EditProfileView();
                EditProfileController editProfileController = new EditProfileController(editProfileView);
                editProfileView.setVisible(true);
                break;
        }
    }

    public static void closeScene(SceneName sceneName) {
        switch (sceneName) {
            case CONNECTSERVER:
                connectServerView.dispose();
                break;
            case SIGNUP:
                signupView.dispose();
                break;
            case LOGIN:
                loginView.dispose();
                break;
            case MAINMENU:
                mainMenuView.dispose();
                break;
            case ROOM:
                roomView.dispose();
                break;
            case INGAME:
                inGameView.dispose();
                break;
            case STAT:
                playerStatView.dispose();
                break;
            case EDIT:
                editProfileView.dispose();
                break;
        }
    }

    public static void main(String[] args) {
        ClientMain clientMain = new ClientMain();
    }

    
}
