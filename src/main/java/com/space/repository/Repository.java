package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;

public interface Repository extends CrudRepository<Ship, Long> {

    /*@Transactional
    @Modifying
    @Query("update Ship s set s.name = :shipName, s.planet = :planet," +
            "s.shipType = :shipType, s.prodDate = :prodDate, s.isUsed = :isUsed," +
            "s.speed = :speed, s.crewSize = :crewSize where s.id = :id")
    void updateShip(@Param("id") Long id, @Param("shipName") String name,
                    @Param("planet") String planet, @Param("shipType") String shipTypeStr,
                    @Param("prodDate") Date prodDate, @Param("isUsed") boolean isUsed,
                    @Param("speed") Double speed, @Param("crewSize") Integer crewSize);
     Имя
       планета
       тип
       дата производства
       б/у
       скорость
       размер команды
*/
}
