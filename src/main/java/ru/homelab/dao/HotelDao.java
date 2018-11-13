package ru.homelab.dao;


import ru.homelab.model.Hotel;

import java.util.List;

public interface HotelDao {

    String save(Hotel hotel);

    Hotel getById(String id);

    void update(Hotel hotel);

    void delete(String id);

    List<Hotel> getAll();
}
