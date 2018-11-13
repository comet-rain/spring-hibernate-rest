package ru.homelab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.homelab.model.Hotel;
import ru.homelab.service.HotelService;

import java.util.List;

@RestController
@RequestMapping("hotels")
public class HotelController {
    private HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> getAll() {
        List<Hotel> hotels = hotelService.getAll();
        return new ResponseEntity<>(hotels, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Hotel> getById(@PathVariable("id") String id) {
        Hotel hotel = hotelService.getById(id);
        if (hotel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(hotel, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<String> create(@RequestBody Hotel hotel) {
        if (hotel.getId() != null) {
            Hotel hotelFromDB = hotelService.getById(hotel.getId());
            if (hotelFromDB != null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        String id = hotelService.save(hotel);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody Hotel hotel) {
        Hotel hotelFromDB = hotelService.getById(id);
        if (hotelFromDB == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        hotelService.update(hotel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        Hotel hotel = hotelService.getById(id);
        if (hotel == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        hotelService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
