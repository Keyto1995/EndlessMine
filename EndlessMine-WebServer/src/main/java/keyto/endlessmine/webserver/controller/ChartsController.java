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
import keyto.endlessmine.dbservice.entity.Player;
import keyto.endlessmine.dbservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Keyto
 */
@Controller
@RequestMapping("/charts")
public class ChartsController {

    @Autowired
    PlayerService playerService;

    @RequestMapping("")
    public ModelAndView charts() {
        List<Player> findTop10ByOrderByScoreDesc = playerService.findTop10ByOrderByScoreDesc();
        ModelAndView mv = new ModelAndView("charts");
        mv.addObject("Top10List", findTop10ByOrderByScoreDesc);
        return mv;
    }

}
