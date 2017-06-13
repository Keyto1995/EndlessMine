/*
 * Copyright (C) 2017 Keyto
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * E-mail: keyto1995@outlook.com
 */
package keyto.endlessmine.webserver.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import keyto.endlessmine.common.block.IBlock;
import keyto.endlessmine.common.block.IBlockInfo;
import keyto.endlessmine.common.coordinate_system.impl.BlockPoint;
import keyto.endlessmine.common.coordinate_system.impl.ChunkPoint;
import keyto.endlessmine.common.mouse.MouseButton;
import keyto.endlessmine.dbservice.entity.Player;
import keyto.endlessmine.dbservice.service.PlayerService;
import keyto.endlessmine.gameserver.manager.BlockManager;
import keyto.endlessmine.webserver.domain.DoActionMessage;
import keyto.endlessmine.webserver.domain.GetChunkMessage;
import keyto.endlessmine.webserver.domain.SafePlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Keyto
 */
@Controller
@RequestMapping("/game")
public class GameController {

    @Autowired
    BlockManager blockManager;

    @Autowired
    PlayerService playerService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RequestMapping("/doAction")
    @ResponseBody
    long doAction(DoActionMessage doActionMessage, HttpServletRequest request) {
        Player player = getPlayerFromSecurity(request);
        ChunkPoint chunkPoint = new ChunkPoint(doActionMessage.getChunkPointX(), doActionMessage.getChunkPointY());
        BlockPoint blockPoint = new BlockPoint(chunkPoint, doActionMessage.getBlockX(), doActionMessage.getBlockY());
        MouseButton mouseButton = MouseButton.valueOf(doActionMessage.getMouseButton());
        List<IBlock> doActionResult = blockManager.doAction(blockPoint, mouseButton, player.getId());
        //TODO:计分功能
        int resultSize = doActionResult.size();
        if (resultSize > 0) {
            IBlock ib = doActionResult.get(0);
            if (mouseButton == MouseButton.PRIMARY) {
                if (ib.getBlockInfo().isBomb()) {
                    updateScore(player, -5);
                }
            } else if (ib.getBlockInfo().isBomb()) {
                updateScore(player, 1);
            } else {
                updateScore(player, -2);
            }
        }
        //
        for (IBlock block : doActionResult) {
            messagingTemplate.convertAndSend("/game/updateBlock", block);
        }

        updateCharts();
        return player.getScore();
    }

    private Player getPlayerFromSecurity(HttpServletRequest request) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Player player = (Player) securityContextImpl.getAuthentication().getPrincipal();
        return player;
    }

    private void updateScore(Player player, long deltaScore) {
        player.setScore(player.getScore() + deltaScore);
        playerService.updateScoreById(player.getId(), player.getScore());
    }

    @RequestMapping("/getChunk")
    @ResponseBody
    IBlockInfo[][] getChunk(GetChunkMessage getChunkMessage) {
        ChunkPoint chunkPoint = new ChunkPoint(getChunkMessage.getChunkPointX(), getChunkMessage.getChunkPointY());

        return blockManager.getEntireBlockInfosOfChunk(chunkPoint);
    }

    @RequestMapping("/updateCharts")
    @ResponseBody
    public List<SafePlayer> updateCharts() {
        List<Player> findTop10ByOrderByScoreDesc = playerService.findTop10ByOrderByScoreDesc();
        List<SafePlayer> safeResult = new ArrayList<>();
        for (Player player : findTop10ByOrderByScoreDesc) {
            SafePlayer sp = new SafePlayer();
            sp.setName(player.getName());
            sp.setScore(player.getScore());
            safeResult.add(sp);
        }
        messagingTemplate.convertAndSend("/charts/updateList", safeResult);
        return safeResult;
    }
}
