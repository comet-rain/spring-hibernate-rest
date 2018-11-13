package ru.homelab.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.homelab.dao.HotelDao;
import ru.homelab.model.Hotel;

import java.util.List;

@Repository
public class HotelDaoImpl implements HotelDao {
    private SessionFactory sessionFactory;

    @Autowired
    public HotelDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public String save(Hotel hotel) {
        sessionFactory.getCurrentSession().save(hotel);
        return hotel.getId();
    }

    @Override
    public Hotel getById(String id) {
        return sessionFactory.getCurrentSession().get(Hotel.class, id);
    }

    @Override
    public void update(Hotel hotel) {
        sessionFactory.getCurrentSession().update(hotel);
    }

    @Override
    public void delete(String id) {
        Hotel hotel = getById(id);
        sessionFactory.getCurrentSession().delete(hotel);
    }

    @Override
    public List<Hotel> getAll() {
        Session session = sessionFactory.getCurrentSession();
        List<Hotel> hotels = session.createQuery("from Hotel").list();

        return hotels;
    }
}
