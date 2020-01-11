package com.space.service;

import com.space.model.Ship;

import java.util.List;

public interface ShipService {
    List<Ship> getAllShips();
    Ship getShipByID(Long id);
    void addShip(Ship ship);
    void deleteShipById(Long id);
    Ship updateShipById(Ship ship);
}
