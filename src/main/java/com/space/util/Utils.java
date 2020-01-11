package com.space.util;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Utils {

    private final Long MIN_DATE = new Long("26192210400000");  //26192210400000
    private final Long MAX_DATE = new Long("33134709600000");  //33134709600000
    private final Double MIN_SPEED = new Double("0");
    private final Double MAX_SPEED = new Double("1");
    private final Integer MIN_CREW_SIZE = new Integer("0");
    private final Integer MAX_CREW_SIZE = new Integer("10000");

    public boolean checkValid(String id) {
        Long shipID;
        if (id.isEmpty()) return false;
        try {
            shipID = new Long(id);
        } catch (Exception ex) {
            return false;
        }
        if (shipID <= 0) return false;
        return true;
    }

    public Ship fromShipToShip(Ship from, Ship to) {
        if (from.getName() != null) to.setName(from.getName());
        if (from.getPlanet() != null) to.setPlanet(from.getPlanet());
        if (from.getShipType() != null) to.setShipType(from.getShipType());
        if (from.getProdDate() != null) to.setProdDate(from.getProdDate());
        if (from.isUsed() != null) to.setUsed(from.isUsed());
        if (from.getSpeed() != null) to.setSpeed(from.getSpeed());
        if (from.getCrewSize() != null) to.setCrewSize(from.getCrewSize());

        to.setRating(rateCounting(to));
        return to;
    }

    public boolean allCheck(Ship ship) {
        if (ship.getName() == null & ship.getSpeed() == null
                & ship.getPlanet() == null & ship.getProdDate() == null
                & ship.getCrewSize() == null & ship.getRating() == null
                & ship.getId() == null & ship.getShipType() == null
                & (ship.isUsed() == null || !ship.isUsed()) & ship.getSpeed() == null) return false;
        return true;
    }

    public boolean onlyOneCheck(Ship ship) {
        if (ship.getName() == null || ship.getSpeed() == null
                || ship.getPlanet() == null || ship.getProdDate() == null
                || ship.getCrewSize() == null || ship.getShipType() == null
                || ship.getSpeed() == null) return false;
        return true;
    }

    public boolean checkNameAndPlanet(String name) {
        if (!name.isEmpty() && name.length() < 51) return true;
        return false;
    }

    public boolean checkDate(Date date) {
        if (date.getTime() <= MAX_DATE
                & date.getTime() >= MIN_DATE)
            return true;
        return false;
    }

    public boolean checkSpeed(Double speed) {
        if (speed <= MAX_SPEED
                & speed >= MIN_SPEED)
            return true;
        return false;
    }

    public boolean checkCrewSize(int size) {
        if (size <= MAX_CREW_SIZE
                && size >= MIN_CREW_SIZE)
            return true;
        return false;
    }

    public Double rateCounting(Ship ship) {
        int maxYear = getYear(MAX_DATE);

        Double speed = ship.getSpeed();
        Double k = ship.isUsed() ? 0.5 : 1;
        int prodYear = getYear(ship.getProdDate().getTime());
        Double rate = (80 * speed * k) /
                (maxYear - prodYear + 1);
        return round(rate);
    }

    public Integer getYear(Long millis) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.YEAR);
    }

    public Double round(double targetNumber) {
        BigDecimal scaleNumber = new BigDecimal(targetNumber);
        scaleNumber = scaleNumber.setScale(2, RoundingMode.HALF_UP);
        return scaleNumber.doubleValue();
    }

    public List<Ship> removeIfParams(String name, String planet, ShipType shipType, Long after,
                                     Long before, Boolean isUsed, Double minSpeed, Double maxSpeed,
                                     Integer minCrewSize, Integer maxCrewSize,
                                     Double minRating, Double maxRating, List<Ship> ships) {
        ships.removeIf(ship -> !ship.getName().contains(name));
        ships.removeIf(ship -> !ship.getPlanet().contains(planet));
        if (shipType != null) ships.removeIf(ship -> ship.getShipType() != shipType);
        if (isUsed != null) ships.removeIf(ship -> ship.isUsed() != isUsed);

        if (after != null) ships.removeIf(ship -> ship.getProdDate().getTime() < after);
        if (before != null) ships.removeIf(ship -> ship.getProdDate().getTime() > before);


        if (minSpeed != null) ships.removeIf(ship -> ship.getSpeed() < minSpeed);
        if (maxSpeed != null) ships.removeIf(ship -> ship.getSpeed() > maxSpeed);

        if (minCrewSize != null) ships.removeIf(ship -> ship.getCrewSize() < minCrewSize);
        if (maxCrewSize != null) ships.removeIf(ship -> ship.getCrewSize() > maxCrewSize);

        if (minRating != null) ships.removeIf(ship -> ship.getRating() < minRating);
        if (maxRating != null) ships.removeIf(ship -> ship.getRating() > maxRating);

        return ships;
    }

    public List<Ship> sortShipList(ShipOrder shipOrder, List<Ship> ships) {
        if (shipOrder == ShipOrder.ID) ships.sort(Comparator.comparing(ship -> ship.getId()));
        if (shipOrder == ShipOrder.SPEED) ships.sort(Comparator.comparing(ship -> ship.getSpeed()));
        if (shipOrder == ShipOrder.DATE) ships.sort(Comparator.comparing(ship -> ship.getProdDate()));
        if (shipOrder == ShipOrder.RATING) ships.sort(Comparator.comparing(ship -> ship.getRating()));
        return ships;
    }
}
