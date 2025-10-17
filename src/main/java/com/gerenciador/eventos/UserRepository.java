package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para gerenciar usuários e suas validações
 */
public class UserRepository {
    
    // Lista estática para simular banco de dados
    private static List<User> users = new ArrayList<>();
    
    /**
     * Verifica se já existe um usuário com o email fornecido
     * @param email O email a ser verificado
     * @return true se o email já existe, false caso contrário
     */
    public static boolean existsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return users.stream()
                .anyMatch(user -> user.getEmail() != null && 
                         user.getEmail().equalsIgnoreCase(email.trim()));
    }
    
    /**
     * Verifica se já existe um usuário com o nome fornecido
     * @param name O nome a ser verificado
     * @return true se o nome já existe, false caso contrário
     */
    public static boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        return users.stream()
                .anyMatch(user -> user.getName() != null && 
                         user.getName().equalsIgnoreCase(name.trim()));
    }
    
    /**
     * Verifica se já existe um usuário com o CPF fornecido
     * @param cpf O CPF a ser verificado
     * @return true se o CPF já existe, false caso contrário
     */
    public static boolean existsByCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        
        return users.stream()
                .anyMatch(user -> user.getCpf() != null && 
                         user.getCpf().equals(cpf.trim()));
    }
    
    /**
     * Busca um usuário pelo email
     * @param email O email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    public static Optional<User> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return users.stream()
                .filter(user -> user.getEmail() != null && 
                       user.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst();
    }
    
    /**
     * Busca um usuário pelo nome
     * @param name O nome do usuário
     * @return Optional contendo o usuário se encontrado
     */
    public static Optional<User> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return users.stream()
                .filter(user -> user.getName() != null && 
                       user.getName().equalsIgnoreCase(name.trim()))
                .findFirst();
    }
    
    /**
     * Salva um usuário no repositório
     * @param user O usuário a ser salvo
     * @return O usuário salvo com ID gerado
     */
    public static User save(User user) {
        if (user.getId() == null) {
            // Gerar novo ID
            Long newId = users.stream()
                    .mapToLong(u -> u.getId() != null ? u.getId() : 0L)
                    .max()
                    .orElse(0L) + 1L;
            user.setId(newId);
        }
        
        users.add(user);
        return user;
    }
    
    /**
     * Obtém todos os usuários
     * @return Lista de todos os usuários
     */
    public static List<User> findAll() {
        return new ArrayList<>(users);
    }
    
    /**
     * Remove todos os usuários (útil para testes)
     */
    public static void deleteAll() {
        users.clear();
    }
    
    /**
     * Conta o total de usuários
     * @return Número total de usuários
     */
    public static long count() {
        return users.size();
    }
}
