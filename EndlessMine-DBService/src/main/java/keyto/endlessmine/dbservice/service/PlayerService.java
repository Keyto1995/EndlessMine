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
package keyto.endlessmine.dbservice.service;

import java.util.List;
import keyto.endlessmine.dbservice.entity.Player;
import keyto.endlessmine.dbservice.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Keyto
 */
@Service
public class PlayerService implements UserDetailsService {

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * @return the playerRepository
     */
    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    /**
     * @param playerRepository
     */
    public void setUserInfoRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

//    @CachePut(value = "player", key = "#player.name")
    public Player save(Player player) {
        Player save = playerRepository.save(player);
        return save;
    }

    public Player findByNameAndPassword(String name, String password) {
        Player player = playerRepository.findByNameAndPassword(name, password);
        return player;
    }

    public boolean existsByName(String name) {
        Boolean existsByName = playerRepository.existsByName(name);
        return existsByName;
    }

    public boolean existsByEmail(String email) {
        Boolean existsByEmail = playerRepository.existsByEmail(email);
        return existsByEmail;
    }

    public int updateScoreById(long id, long score) {
        int updateScoreById = playerRepository.updateScoreById(id, score);
        return updateScoreById;
    }
    
    public List<Player> findTop10ByOrderByScoreDesc(){
        List<Player> findTop10ByOrderByScoreDesc = playerRepository.findTop10ByOrderByScoreDesc();
        return findTop10ByOrderByScoreDesc;
    }

    @Override
//    @Cacheable(value = "player", key = "#player.name")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByName(username);
        if (null == player) {
            System.out.println("用户名不存在：" + username);
            throw new UsernameNotFoundException("用户名不存在：" + username);
        }
        return player;
    }
}
