package com.space.service;

import com.space.model.Ship;
import com.space.repository.Repository;
import com.space.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {
    private Repository repository;

    @Autowired
    public ShipServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public List<Ship> getAllShips() {
        return (List<Ship>) repository.findAll();
    }

    @Override
    public Ship getShipByID(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void addShip(Ship ship) {
        Utils utils = new Utils();
        ship.setRating(utils.rateCounting(ship));
        repository.save(ship);
    }

    @Override
    public void deleteShipById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Ship updateShipById(Ship ship) {
        return repository.save(ship);
    }


}
