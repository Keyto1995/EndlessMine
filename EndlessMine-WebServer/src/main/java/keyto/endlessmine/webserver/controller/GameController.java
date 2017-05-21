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

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import keyto.endlessmine.common.block.IBlock;
import keyto.endlessmine.common.block.IBlockInfo;
import keyto.endlessmine.common.coordinate_system.impl.BlockPoint;
import keyto.endlessmine.common.coordinate_system.impl.ChunkPoint;
import keyto.endlessmine.common.mouse.MouseButton;
import keyto.endlessmine.dbservice.entity.Player;
import keyto.endlessmine.gameserver.manager.BlockManager;
import keyto.endlessmine.webserver.domain.DoActionMessage;
import keyto.endlessmine.webserver.domain.GetChunkMessage;
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
    private SimpMessagingTemplate messagingTemplate;

    @RequestMapping("/demo_test_show")
    @ResponseBody
    void demo_test_show(DoActionMessage doActiveMessage, HttpServletRequest request) {
        Player player = getPlayerFromSecurity(request);
        ChunkPoint chunkPoint = new ChunkPoint(doActiveMessage.getChunkPointX(), doActiveMessage.getChunkPointY());
        BlockPoint blockPoint = new BlockPoint(chunkPoint, doActiveMessage.getBlockX(), doActiveMessage.getBlockY());
        MouseButton mouseButton = MouseButton.valueOf(doActiveMessage.getMouseButton());
        List<IBlock> doActionResult = blockManager.doAction(blockPoint, mouseButton, player.getId());
        for (IBlock block : doActionResult) {
            messagingTemplate.convertAndSend("/game/updateBlock", block);
        }
    }

    private Player getPlayerFromSecurity(HttpServletRequest request) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Player player = (Player) securityContextImpl.getAuthentication().getPrincipal();
        return player;
    }

    @RequestMapping("/demo_test_show_chunk")
    @ResponseBody
    IBlockInfo[][] demo_test_show_chunk(GetChunkMessage getChunkMessage) {
        ChunkPoint chunkPoint = new ChunkPoint(getChunkMessage.getChunkPointX(), getChunkMessage.getChunkPointY());

        return blockManager.getEntireBlockInfosOfChunk(chunkPoint);
    }
}
