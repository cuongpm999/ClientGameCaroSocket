/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.ptit.controller.viewhandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import vn.ptit.ClientMain;
import vn.ptit.model.PlayerStat;
import vn.ptit.view.scene.PlayerStatView;
import vn.ptit.view.scene.SignupView;

/**
 *
 * @author Cuong Pham
 */
public class PlayerStatController {

    private PlayerStatView playerStatView;
    private List<PlayerStat> playerStats;

    public PlayerStatController(PlayerStatView playerStatView) {
        this.playerStatView = playerStatView;
        playerStats = ClientMain.socketHandler.readMessage.getPlayerStats();
        DefaultTableModel model = (DefaultTableModel) playerStatView.getjTable1().getModel();
        model.setRowCount(0);
        Collections.sort(playerStats, new Comparator<PlayerStat>(){
            @Override
            public int compare(PlayerStat o1, PlayerStat o2) {
                return o2.getPoint()-o1.getPoint();
            }
            
        });
        int stt=1;
        for (PlayerStat playerStat : playerStats) {
            playerStat.setStt(stt);
            model.addRow(playerStat.toObject());
            stt++;
        }
        this.playerStatView.addListener(new PlayerStatListener());

    }
    
    class PlayerStatListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ClientMain.openScene(ClientMain.SceneName.MAINMENU);
            ClientMain.socketHandler.sendMessage.listOnline();
            ClientMain.socketHandler.sendMessage.listRoom();
            ClientMain.socketHandler.sendMessage.checkFriend();
            ClientMain.closeScene(ClientMain.SceneName.STAT);
        }

    }
}
