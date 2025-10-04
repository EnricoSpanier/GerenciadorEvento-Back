package com.gerenciador.eventos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class UserTest {
    
    public UserTest() {
    }

    @BeforeEach
    public void setUp() {
        // Limpa o repositório antes de cada teste
        UserRepository.deleteAll();
    }
    
    User objUser = new User();

    //testando os Getters

    @Test
    public void testGetId() {
        objUser.setId(1L);
        assertEquals(1L, objUser.getId());
    }
    @Test
    public void testGetName() {
        objUser.setName("John Doe");
        assertEquals("John Doe", objUser.getName());
    }
    @Test
    public void testGetEmail() {
        objUser.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", objUser.getEmail());
    }
    @Test
    public void testGetFone() {
        objUser.setFone("1234567890");
        assertEquals("1234567890", objUser.getFone());
    }  
    @Test
    public void testGetPassword() {
        objUser.setPassword("password123");
        assertEquals("password123", objUser.getPassword());
    }   
    @Test
    public void testGetCpf() {
        objUser.setCpf("123.456.789-00");
        assertEquals("123.456.789-00", objUser.getCpf());
    }   
    @Test
    public void testGetBirthDate() {
        objUser.setBirthDate("01/01/1990");
        assertEquals("01/01/1990", objUser.getBirthDate());
    }  
    @Test
    public void testGetIsAdmin() {
        objUser.setIsAdmin(true);
        assertTrue(objUser.getIsAdmin());
    }   
    @Test
    public void testGetIsActive() {
        objUser.setIsActive(true);
        assertTrue(objUser.getIsActive());
    }   
    @Test
    public void testGetCreatedAt() {
        objUser.setCreatedAt("2023-10-01T12:00:00");
        assertEquals("2023-10-01T12:00:00", objUser.getCreatedAt());
    }  
    @Test
    public void testGetUpdatedAt() {
        objUser.setUpdatedAt("2023-10-02T12:00:00");
        assertEquals("2023-10-02T12:00:00", objUser.getUpdatedAt());
    }

    //testando regra de não repeticão de email e nome

    @Test
    public void testValidateDuplicates() {
        // Cria e salva o primeiro usuário
        User firstUser = new User();
        firstUser.setName("John Doe");
        firstUser.setEmail("john.doe@example.com");
        firstUser.createUser();

        // Tenta criar um segundo usuário com o mesmo nome e email
        User secondUser = new User();
        secondUser.setName("John Doe");
        secondUser.setEmail("john.doe@example.com");

        // Verifica se a validação de duplicatas impede a criação do segundo usuário
        assertThrows(IllegalArgumentException.class, () -> {
            secondUser.createUser();
        });
    }

    @Test
    public void testValidateDuplicateEmail() {
        // Cria primeiro usuário
        User firstUser = new User();
        firstUser.setName("John Doe");
        firstUser.setEmail("test@example.com");
        firstUser.createUser();

        // Tenta criar segundo usuário com mesmo email
        User secondUser = new User();
        secondUser.setName("Jane Smith");
        secondUser.setEmail("test@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            secondUser.createUser();
        });
        
        assertTrue(exception.getMessage().contains("Email já existente"));
    }

    @Test
    public void testValidateDuplicateName() {
        // Cria primeiro usuário
        User firstUser = new User();
        firstUser.setName("John Doe");
        firstUser.setEmail("john@example.com");
        firstUser.createUser();

        // Tenta criar segundo usuário com mesmo nome
        User secondUser = new User();
        secondUser.setName("John Doe");
        secondUser.setEmail("jane@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            secondUser.createUser();
        });
        
        assertTrue(exception.getMessage().contains("Nome já existente"));
    }

    @Test
    public void testValidateDuplicateCpf() {
        // Cria primeiro usuário
        User firstUser = new User();
        firstUser.setName("John Doe");
        firstUser.setEmail("john@example.com");
        firstUser.setCpf("123.456.789-00");
        firstUser.createUser();

        // Tenta criar segundo usuário com mesmo CPF
        User secondUser = new User();
        secondUser.setName("Jane Smith");
        secondUser.setEmail("jane@example.com");
        secondUser.setCpf("123.456.789-00");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            secondUser.createUser();
        });
        
        assertTrue(exception.getMessage().contains("CPF"));
    }

    @Test
    public void testSuccessfulUserCreation() {
        // Teste de criação bem-sucedida
        User user = new User();
        user.setName("Valid User");
        user.setEmail("valid@example.com");
        user.setCpf("111.222.333-44");
        
        User savedUser = user.createUser();
        
        assertNotNull(savedUser.getId());
        assertEquals("Valid User", savedUser.getName());
        assertEquals("valid@example.com", savedUser.getEmail());
        assertEquals(1L, UserRepository.count());
    }

}