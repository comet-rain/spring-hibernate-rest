package ru.homelab.service;


import ru.homelab.model.Hotel;

import java.util.List;

public interface HotelService {

    String save(Hotel hotel);

    Hotel getById(String id);

    void update(Hotel hotel);

    void delete(String id);

    List<Hotel> getAll();
}
