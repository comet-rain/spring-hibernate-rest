package ru.homelab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.homelab.model.Hotel;
import ru.homelab.model.Point;
import ru.homelab.model.Service;
import ru.homelab.model.Site;
import ru.homelab.service.HotelService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class HotelControllerTest {
    @Mock
    private HotelService hotelService;
    @InjectMocks
    private HotelController hotelController;
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private Hotel hotel;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(hotelController)
                .build();
        initHotels();
    }

    @Test
    public void getAll() throws Exception {
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(hotel);
        hotels.add(new Hotel());

        when(hotelService.getAll()).thenReturn(hotels);

        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)));

        verify(hotelService).getAll();
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    public void getById() throws Exception {
        String id = hotel.getId();
        when(hotelService.getById(id)).thenReturn(hotel);

        MvcResult result = mockMvc.perform(get("/hotels/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        Hotel hotel = mapper.readValue(json, Hotel.class);

        assertEquals(this.hotel, hotel);

        verify(hotelService).getById(id);
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    public void getByIdFail404() throws Exception {
        String id = "3";
        when(hotelService.getById(id)).thenReturn(null);

        mockMvc.perform(get("/hotels/{id}", id))
                .andExpect(status().isNotFound());

        verify(hotelService).getById(id);
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    public void create() throws Exception {
        hotel.setId(null);

        mockMvc.perform(post("/hotels")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(hotel)))
                .andExpect(status().isCreated());

        verify(hotelService).save(hotel);
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    public void createFail409() throws Exception {
        String id = hotel.getId();
        when(hotelService.getById(id)).thenReturn(hotel);

        mockMvc.perform(post("/hotels")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(hotel)))
                .andExpect(status().isConflict());

        verify(hotelService).getById(id);
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    public void update() throws Exception {
        String id = hotel.getId();
        when(hotelService.getById(id)).thenReturn(hotel);
        doNothing().when(hotelService).update(hotel);
        mockMvc.perform(put("/hotels/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(hotel)))
                .andExpect(status().isOk());

        verify(hotelService).getById(id);
        verify(hotelService).update(hotel);
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    public void updateFail404() throws Exception {
        String id = "3";
        hotel.setId(id);
        when(hotelService.getById(id)).thenReturn(null);

        mockMvc.perform(put("/hotels/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(hotel)))
                .andExpect(status().isNotFound());

        verify(hotelService).getById(id);
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    public void delete() throws Exception {
        String id = hotel.getId();
        when(hotelService.getById(id)).thenReturn(hotel);
        doNothing().when(hotelService).delete(hotel.getId());
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/hotels/{id}", id))
                .andExpect(status().isOk());

        verify(hotelService).getById(id);
        verify(hotelService).delete(id);
        verifyNoMoreInteractions(hotelService);
    }

    @Test
    public void deleteFail404() throws Exception {
        String id = "3";
        hotel.setId(id);
        when(hotelService.getById(id)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/hotels/{id}", id))
                .andExpect(status().isNotFound());

        verify(hotelService).getById(id);
        verifyNoMoreInteractions(hotelService);
    }

    private void initHotels() {
        Set<Service> services = new HashSet<>();
        services.add(Service.bar);
        services.add(Service.restaurant);
        services.add(Service.beach);
        services.add(Service.fitness);
        services.add(Service.transfer);

        Point point = Point.builder()
                .latitude(43.56F)
                .longitude(22.01F)
                .build();

        Site site = Site.builder()
                .siteOne("https://habr.com")
                .siteTwo("https://ya.ru")
                .build();

        hotel = Hotel.builder()
                .id("1")
                .name("Друзья на Фонтанке")
                .catid("78322")
                .point(point)
                .addr("Москва, Измайловское шоссе 71А")
                .img("http://mysite/imgs/img")
                .site(site)
                .services(services)
                .build();
    }
}