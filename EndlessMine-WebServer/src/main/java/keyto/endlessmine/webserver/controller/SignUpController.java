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

import javax.servlet.http.HttpServletRequest;
import keyto.endlessmine.dbservice.entity.Player;
import keyto.endlessmine.dbservice.service.PlayerService;
import keyto.endlessmine.webserver.domain.DoLoginMessage;
import keyto.endlessmine.webserver.domain.SignUpMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/signUp")
public class SignUpController {

    @Autowired
    PlayerService playerService;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @RequestMapping("")
    String signUp() {
        return "signUp";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    ModelAndView signUp(SignUpMessage signUpMessage, HttpServletRequest request) {
        System.out.println("signUp:" + signUpMessage);
        if (signUpMessage.getName().equals("")
                || signUpMessage.getName().matches("[@]")
                || signUpMessage.getPassword().equals("")
                || existsByName(signUpMessage.getName())
                || signUpMessage.getEmail().equals("") ? false : existsByEmail(signUpMessage.getEmail())) {
            ModelAndView mv = new ModelAndView("signUp");
            mv.addObject("name", signUpMessage.getName());
            mv.addObject("email", signUpMessage.getEmail());
            return mv;
        }
        try {
            Player player = new Player(null, signUpMessage.getName(), signUpMessage.getEmail(), signUpMessage.getPassword());
            Player save = playerService.save(player);
            DoLoginMessage doLoginMessage = new DoLoginMessage();
            doLoginMessage.setUsername(signUpMessage.getName());
            doLoginMessage.setPassword(signUpMessage.getPassword());
            return loginAfterSignUp(doLoginMessage, request);
        } catch (final Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private ModelAndView loginAfterSignUp(DoLoginMessage doLoginMessage,
                                          HttpServletRequest request) {
        System.out.println("loginAfterSignUp");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                doLoginMessage.getUsername(), doLoginMessage.getPassword());
        try {
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authenticatedUser = authenticationManager
                    .authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            return new ModelAndView(new RedirectView("login?error"));
        }

        return new ModelAndView(new RedirectView(""));
    }

    @RequestMapping(value = "/existsByName")
    @ResponseBody
    Boolean existsByName(String name) {
        return playerService.existsByName(name);
    }

    @RequestMapping(value = "/existsByEmail")
    @ResponseBody
    Boolean existsByEmail(String email) {
        return playerService.existsByEmail(email);
    }
}
