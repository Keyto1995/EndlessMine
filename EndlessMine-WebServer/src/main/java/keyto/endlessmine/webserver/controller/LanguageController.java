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

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Keyto
 */
@Controller
public class LanguageController {

    @Autowired
    LocaleResolver localeResolver;

    @RequestMapping("language")
    public ModelAndView language(HttpServletRequest request, HttpServletResponse response, String language) {
        language = language.toLowerCase();
        if (language == null || language.equals("")) {
            return new ModelAndView("redirect:/");
        } else if (language.equals("zh_cn")) {
            localeResolver.setLocale(request, response, Locale.CHINA);
        } else if (language.equals("en")) {
            localeResolver.setLocale(request, response, Locale.ENGLISH);
        } else {
            localeResolver.setLocale(request, response, Locale.ENGLISH);
        }

        return new ModelAndView("redirect:/");
    }
}
