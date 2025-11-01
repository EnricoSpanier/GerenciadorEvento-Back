package com.gerenciador.eventos.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gerenciador.eventos.Object.User;
import com.gerenciador.eventos.Repository.UserRepository;

/**
 * Service - lógica de negócio e validações
 * Coordena as operações entre Controller e Repository
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Criar novo usuário com validações
     */
    public User createUser(User user) {
        validateRequiredFields(user);
        validateDuplicates(user);
        user.setIsActive(true);
        return userRepository.save(user);
    }

    /**
     * Criar usuário completo (método helper)
     */
    public User createNewUser(String name, String email, String password, String fone) {
        User user = new User(name, email, fone, password, null);
        return createUser(user);
    }

    /**
     * Verificar se usuário pode ser criado (sem lançar exceção)
     */
    public boolean canBeCreated(User user) {
        try {
            validateRequiredFields(user);
            validateDuplicates(user);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Obter erros de validação em formato String
     */
    public String getValidationErrors(User user) {
        StringBuilder errors = new StringBuilder();
        
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            errors.append("Nome não pode estar vazio. ");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            errors.append("Email não pode estar vazio. ");
        }
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty() 
            && userRepository.emailExists(user.getEmail())) {
            errors.append("Email '").append(user.getEmail()).append("' já está cadastrado. ");
        }
        if (user.getName() != null && !user.getName().trim().isEmpty() 
            && userRepository.nameExists(user.getName())) {
            errors.append("Nome '").append(user.getName()).append("' já está cadastrado. ");
        }
        if (user.getFone() != null && !user.getFone().trim().isEmpty() 
            && userRepository.foneExists(user.getFone())) {
            errors.append("Telefone '").append(user.getFone()).append("' já está cadastrado. ");
        }
        
        return errors.toString().trim();
    }

    /**
     * Buscar usuário por ID
     */
    public User findById(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        return user;
    }

    /**
     * Buscar usuário por email
     */
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado com email: " + email);
        }
        return user;
    }

    /**
     * Atualizar usuário
     */
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        validateRequiredFields(user);
        return userRepository.update(user);
    }

    /**
     * Desativar usuário (soft delete)
     */
    public void deactivateUser(Long id) {
        userRepository.softDelete(id);
    }

    // ========== Validações Privadas ==========

    private void validateRequiredFields(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode estar vazio");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode estar vazio");
        }
    }

    private void validateDuplicates(User user) {
        if (user.getName() != null && !user.getName().trim().isEmpty() 
            && userRepository.nameExists(user.getName())) {
            throw new IllegalArgumentException("Nome já existente: " + user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty() 
            && userRepository.emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email já existente: " + user.getEmail());
        }
        if (user.getFone() != null && !user.getFone().trim().isEmpty() 
            && userRepository.foneExists(user.getFone())) {
            throw new IllegalArgumentException("Telefone já existente: " + user.getFone());
        }
    }
}
