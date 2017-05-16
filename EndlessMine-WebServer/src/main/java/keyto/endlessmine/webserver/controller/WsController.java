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

import java.security.Principal;
import keyto.endlessmine.webserver.domain.WorldChatMessage;
import keyto.endlessmine.webserver.domain.WorldChatResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Keyto
 */
@Controller
public class WsController {

    @MessageMapping("/say")
    @SendTo("/worldchat/say")
    public WorldChatResponse say(Principal principal, WorldChatMessage message) {
        return new WorldChatResponse(principal.getName(), message.getMessage());
    }
}
