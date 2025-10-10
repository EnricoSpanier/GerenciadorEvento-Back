package com.gerenciador.eventos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventTest {
    
    public EventTest() {
    }

    @BeforeEach
    public void setUp() {
    }
    
    Event objEvent = new Event();

    //testando os Getters

    @Test
    public void testGetEvent_id() {
        objEvent.setEvent_id(1L);
        assertEquals(1L, objEvent.getEvent_id());
    }

    @Test
    public void testGetCreator_id() {
        objEvent.setCreator_id(1L);
        assertEquals(1L, objEvent.getCreator_id());
    }

    @Test
    public void testGetEvent_name() {
        objEvent.setEvent_name("Event Name");
        assertEquals("Event Name", objEvent.getEvent_name());
    }

    @Test
    public void testGetIs_EAD() {
        objEvent.setIs_EAD(true);
        assertTrue(objEvent.getIs_EAD());
    }

    @Test
    public void testGetAddress() {
        objEvent.setAddress("Address");
        assertEquals("Address", objEvent.getAddress());
    }

    @Test
    public void testGetEvent_date() {
        LocalDateTime date = LocalDateTime.now();
        objEvent.setEvent_date(date);
        assertEquals(date, objEvent.getEvent_date());
    }

    @Test
    public void testGetBuy_time_limit() {
        LocalDateTime limit = LocalDateTime.now();
        objEvent.setBuy_time_limit(limit);
        assertEquals(limit, objEvent.getBuy_time_limit());
    }

    @Test
    public void testGetLot_quantity() {
        objEvent.setLot_quantity(10);
        assertEquals(10, objEvent.getLot_quantity().intValue());
    }

    @Test
    public void testGetQuantity() {
        objEvent.setQuantity(100);
        assertEquals(100, objEvent.getQuantity().intValue());
    }

    @Test
    public void testGetDescription() {
        objEvent.setDescription("Description");
        assertEquals("Description", objEvent.getDescription());
    }

    @Test
    public void testGetPresenters() {
        List<String> presenters = new ArrayList<>();
        presenters.add("Presenter1");
        objEvent.setPresenters(presenters);
        assertEquals(presenters, objEvent.getPresenters());
    }

    @Test
    public void testGetCreatedAt() {
        LocalDateTime created = LocalDateTime.now();
        objEvent.setCreatedAt(created);
        assertEquals(created, objEvent.getCreatedAt());
    }

}
