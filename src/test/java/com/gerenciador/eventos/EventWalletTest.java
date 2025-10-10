package com.gerenciador.eventos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class EventWalletTest {
    
    public EventWalletTest() {
    }

    @BeforeEach
    public void setUp() {
    }
    
    EventWallet objEventWallet = new EventWallet();

    //testando os Getters

    @Test
    public void testGetUserId() {
        objEventWallet.setUserId(1L);
        assertEquals(1L, objEventWallet.getUserId());
    }

    @Test
    public void testGetEventId() {
        objEventWallet.setEventId(1L);
        assertEquals(1L, objEventWallet.getEventId());
    }

}
