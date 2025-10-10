package com.gerenciador.eventos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class MyWalletTest {
    
    public MyWalletTest() {
    }

    @BeforeEach
    public void setUp() {
    }
    
    MyWallet objMyWallet = new MyWallet();

    //testando os Getters

    @Test
    public void testGetUserId() {
        objMyWallet.setUserId(1L);
        assertEquals(1L, objMyWallet.getUserId());
    }

}
