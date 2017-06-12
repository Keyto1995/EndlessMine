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
package keyto.endlessmine.dbservice.repository;

import keyto.endlessmine.dbservice.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Keyto
 */
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByName(String name);

    Player findByNameAndPassword(String name, String password);

    /**
     * 查询是否存在该用户名
     *
     * @param username
     * @return
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM UserInfo u WHERE u.name = :name")
    public Boolean existsByName(@Param("name") String username);

    /**
     * 查询是否存在该邮箱
     *
     * @param email
     * @return
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM UserInfo u WHERE u.email = :email")
    public Boolean existsByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE Player p SET p.score = :score WHERE p.id = :id")
    int updateScoreById(@Param("id") long id, @Param("score") long score);
}
