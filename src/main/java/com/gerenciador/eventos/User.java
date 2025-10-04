package com.gerenciador.eventos;


// teste 5 rezando
import java.time.LocalDateTime;

public class User {

    // Atributos básicos (POJO simples)
    private Long id;              // fazer ser preenchido automaticamente
    private String name;
    private String email;
    private String fone;
    private String password;      // Senha sem cryptografia
    private String cpf;
    private String birthDate;     // Modificado para String para compatibilidade com os testes
    private Boolean isAdmin;
    private Boolean isActive;
    private String createdAt;     // Modificado para String para compatibilidade com os testes
    private String updatedAt;     // Adicionado para compatibilidade com os testes

    //Construtor vazio
    public User() { 
        this.id = null;
        this.name = "";
        this.email = "";
        this.fone = "";
        this.password = "";
        this.cpf = "";
        this.birthDate = "";
        this.isAdmin = false;
        this.isActive = true;
        this.createdAt = "";
        this.updatedAt = "";
    }

    // Construtor completo
    public User(Long id, String name, String email, String fone, String password, String cpf, String birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.fone = fone;
        this.password = password;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.isAdmin = false;
        this.isActive = true;
        this.createdAt = LocalDateTime.now().toString();
        this.updatedAt = LocalDateTime.now().toString();
    }

    // Getters
    public Long getId() { 
        return id; 
    }

    public String getName() { 
        return name; 
    }

    public String getEmail() { 
        return email; 
    }

    public String getFone() { 
        return fone; 
    }

    public String getPassword() { 
        return password; 
    }

    public String getCpf() {
        return cpf;
    }

    public String getBirthDate() { 
        return birthDate; 
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public String getCreatedAt() { 
        return createdAt; 
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public void setEmail(String email) {
        this.email = email;  
    }

    public void setFone(String fone) { 
        this.fone = fone;
    }

    public void setPassword(String password) { 
        this.password = password; 
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setBirthDate(String birthDate) { 
        this.birthDate = birthDate; 
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Verifica se o usuário pode ser criado (versão atualizada com todas as validações)
     * @return true se pode ser criado, false caso contrário
     */
    public boolean canBeCreated() {
        String errors = getValidationErrors();
        return errors.isEmpty();
    }
    
    /**
     * Cria um novo usuário com todas as validações necessárias
     * @return O usuário criado
     * @throws IllegalArgumentException se houver erro de validação
     */
    public User createUser() {
        // Executar todas as validações
        validateUser();
        
        // Define timestamps de criação
        this.createdAt = LocalDateTime.now().toString();
        this.updatedAt = LocalDateTime.now().toString();
        this.isActive = true;
        
        // Salva no repositório
        return UserRepository.save(this);
    }
    
    /**
     * Executa todas as validações do usuário
     * @throws IllegalArgumentException se houver erro de validação
     */
    private void validateUser() {
        validateRequiredFields();
        validateDuplicates();
    }
    
    /**
     * Valida campos obrigatórios
     * @throws IllegalArgumentException se algum campo obrigatório estiver vazio
     */
    private void validateRequiredFields() {
        // Valida nome vazio
        if (this.name == null || this.name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode estar vazio");
        }
        
        // Valida email vazio
        if (this.email == null || this.email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email não pode estar vazio");
        }
    }
    
    /**
     * Valida duplicatas no sistema
     * @throws IllegalArgumentException se encontrar duplicatas
     */
    private void validateDuplicates() {
        // Valida nome já existente
        if (this.name != null && !this.name.trim().isEmpty() && 
            UserRepository.existsByName(this.name)) {
            throw new IllegalArgumentException("Nome já existente: " + this.name);
        }
        
        // Valida email já existente
        if (this.email != null && !this.email.trim().isEmpty() && 
            UserRepository.existsByEmail(this.email)) {
            throw new IllegalArgumentException("Email já existente: " + this.email);
        }
        
        // Valida CPF já existente (se fornecido)
        if (this.cpf != null && !this.cpf.trim().isEmpty() && 
            UserRepository.existsByCpf(this.cpf)) {
            throw new IllegalArgumentException("CPF já existente: " + this.cpf);
        }
    }
    
    /**
     * Verifica quais campos estão com problemas (versão não-destrutiva para consulta)
     * @return String com detalhes dos problemas encontrados
     */
    public String getValidationErrors() {
        StringBuilder errors = new StringBuilder();
        
        // Verifica campos vazios
        if (this.name == null || this.name.trim().isEmpty()) {
            errors.append("Nome não pode estar vazio. ");
        }
        
        if (this.email == null || this.email.trim().isEmpty()) {
            errors.append("Email não pode estar vazio. ");
        }
        
        // Verifica duplicatas
        if (this.email != null && !this.email.trim().isEmpty() && 
            UserRepository.existsByEmail(this.email)) {
            errors.append("Email '").append(this.email).append("' já está cadastrado. ");
        }
        
        if (this.name != null && !this.name.trim().isEmpty() && 
            UserRepository.existsByName(this.name)) {
            errors.append("Nome '").append(this.name).append("' já está cadastrado. ");
        }
        
        if (this.cpf != null && !this.cpf.trim().isEmpty() && 
            UserRepository.existsByCpf(this.cpf)) {
            errors.append("CPF '").append(this.cpf).append("' já está cadastrado. ");
        }
        
        return errors.toString().trim();
    }
    
    /**
     * Método estático para criar usuário com validação completa
     * @param name Nome do usuário
     * @param email Email do usuário
     * @param password Senha do usuário
     * @param cpf CPF do usuário
     * @return O usuário criado
     * @throws IllegalArgumentException se houver erro de validação
     */
    public static User createNewUser(String name, String email, String password, String cpf) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setCpf(cpf);
        
        return user.createUser();
    }
    
    /**
     * Método estático para criar usuário simples (apenas nome e email)
     * @param name Nome do usuário
     * @param email Email do usuário
     * @return O usuário criado
     * @throws IllegalArgumentException se houver erro de validação
     */
    public static User createSimpleUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        
        return user.createUser();
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
