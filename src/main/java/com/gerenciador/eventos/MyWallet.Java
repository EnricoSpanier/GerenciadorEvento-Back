package com.gerenciador.eventos;

public class MyWallet {
    private Long user_id;    


// construtor vazio
    public MyWallet() {
        this.user_id = null;
    }

//construtor completo
    public MyWallet(Long user_id) {
        this.user_id = user_id;
    }

// getters
    public Long getUserId() {
        return user_id;
    }

//setters
    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "MyWallet{" +
                "user_id=" + user_id +
                '}';
    }
}
    