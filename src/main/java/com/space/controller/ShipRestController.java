package com.space.controller;


import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import com.space.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/rest/ships")
public class ShipRestController {
    private final ResponseEntity BAD_REQUEST = new ResponseEntity(HttpStatus.BAD_REQUEST);
    private final ResponseEntity NOT_FOUND = new ResponseEntity(HttpStatus.NOT_FOUND);
    private final ResponseEntity OK = new ResponseEntity(HttpStatus.OK);

    private ShipService service;
    private Utils utils = new Utils();

    public ShipRestController() {
    }

    @Autowired
    public ShipRestController(ShipService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ship> shipsList(@RequestParam(value = "name", defaultValue = "", required = false) String name,
                                @RequestParam(value = "planet", defaultValue = "", required = false) String planet,
                                @RequestParam(value = "shipType", defaultValue = "", required = false) ShipType shipType,
                                @RequestParam(value = "after", required = false) Long after,
                                @RequestParam(value = "before", required = false) Long before,
                                @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                @RequestParam(value = "minRating", required = false) Double minRating,
                                @RequestParam(value = "maxRating", required = false) Double maxRating,
                                @RequestParam(value = "order", defaultValue = "ID") ShipOrder shipOrder,
                                @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {
        List<Ship> ships = utils.removeIfParams(name, planet, shipType, after, before,
                isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize,
                minRating, maxRating, service.getAllShips());
        ships = utils.sortShipList(shipOrder, ships);

        int startSubList = pageNumber * pageSize;
        int endSubList = (startSubList + pageSize) > ships.size() ? ships.size() : (startSubList + pageSize);
        ships.subList(startSubList, endSubList);
        return ships.subList(startSubList, endSubList);
    }

    @GetMapping("/count")
    public Integer shipsCount(@RequestParam(value = "name", defaultValue = "", required = false) String name,
                              @RequestParam(value = "planet", defaultValue = "", required = false) String planet,
                              @RequestParam(value = "shipType", defaultValue = "", required = false) ShipType shipType,
                              @RequestParam(value = "after", required = false) Long after,
                              @RequestParam(value = "before", required = false) Long before,
                              @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                              @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                              @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                              @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                              @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                              @RequestParam(value = "minRating", required = false) Double minRating,
                              @RequestParam(value = "maxRating", required = false) Double maxRating) {
        List<Ship> ships = utils.removeIfParams(name, planet, shipType, after, before,
                isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize,
                minRating, maxRating, service.getAllShips());
        return ships.size();
    }

    @PostMapping()
    public ResponseEntity createShip(@RequestBody Ship ship) {
        if (!utils.onlyOneCheck(ship)) return BAD_REQUEST;
        if (ship.isUsed() == null) ship.setUsed(false);
        if (!utils.checkNameAndPlanet(ship.getName()) || !utils.checkNameAndPlanet(ship.getPlanet())
                || !utils.checkSpeed(ship.getSpeed()) || !utils.checkDate(ship.getProdDate())
                || !utils.checkCrewSize(ship.getCrewSize())) return BAD_REQUEST;

        service.addShip(ship);
        return new ResponseEntity(ship, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteShip(@PathVariable String id) {
        if (!utils.checkValid(id)) return BAD_REQUEST;
        Ship ship = service.getShipByID(Long.parseLong(id));
        if (ship == null) return NOT_FOUND;
        service.deleteShipById(Long.parseLong(id));
        return OK;
    }

    @GetMapping("/{id}")
    public ResponseEntity getShip(@PathVariable String id) {
        if (!utils.checkValid(id)) return BAD_REQUEST;
        Ship ship = service.getShipByID(Long.parseLong(id));
        if (ship == null) return NOT_FOUND;
        return new ResponseEntity(ship, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity updateShip(@PathVariable String id, @RequestBody Ship shipUI) {
        if (!utils.checkValid(id)) return BAD_REQUEST;
        Ship shipDB = service.getShipByID(Long.parseLong(id));
        if (shipDB == null) return NOT_FOUND;

        if (!utils.allCheck(shipUI)) return new ResponseEntity(shipDB, HttpStatus.OK);

        if (shipUI.getName() != null && !utils.checkNameAndPlanet(shipUI.getName())) return BAD_REQUEST;
        if (shipUI.getCrewSize() != null && !utils.checkCrewSize(shipUI.getCrewSize())) return BAD_REQUEST;
        if (shipUI.getProdDate() != null && !utils.checkDate(shipUI.getProdDate())) return BAD_REQUEST;
        if (shipUI.getPlanet() != null && !utils.checkNameAndPlanet(shipUI.getPlanet())) return BAD_REQUEST;
        if (shipUI.getSpeed() != null && !utils.checkSpeed(shipUI.getSpeed())) return BAD_REQUEST;

        return new ResponseEntity(service.updateShipById(utils.fromShipToShip(shipUI, shipDB)), HttpStatus.OK);
    }
}
