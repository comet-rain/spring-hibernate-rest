package ru.homelab.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.homelab.dao.HotelDao;
import ru.homelab.model.Hotel;
import ru.homelab.service.HotelService;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {
    private HotelDao hotelDao;

    @Autowired
    public HotelServiceImpl(HotelDao hotelDao) {
        this.hotelDao = hotelDao;
    }

    @Override
    @Transactional
    public String save(Hotel hotel) {
        return hotelDao.save(hotel);
    }

    @Override
    public Hotel getById(String id) {
        return hotelDao.getById(id);
    }

    @Override
    @Transactional
    public void update(Hotel hotel) {
        hotelDao.update(hotel);
    }

    @Override
    @Transactional
    public void delete(String id) {
        hotelDao.delete(id);
    }

    @Override
    public List<Hotel> getAll() {
        return hotelDao.getAll();
    }
}
