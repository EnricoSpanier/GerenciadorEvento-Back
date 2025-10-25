package com.gerenciador.eventos;

public class EventWallet {
    private Long user_id;
    private Long event_id;    
    
    // construtor vazio
    public EventWallet() {
        this.user_id = null;
        this.event_id = null;
    }  

    //construtor completo
    public EventWallet(Long user_id, Long event_id) {
        this.user_id = user_id;
        this.event_id = event_id;
    }

    // getters
    public Long getUserId() {
        return user_id;
    }

    public Long getEventId() {
        return event_id;
    }

    //setters
    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }

    public void setEventId(Long event_id) {
        this.event_id = event_id;
    }

    @Override
    public String toString() {
        return "EventWallet{" +
                "user_id=" + user_id +
                ", event_id=" + event_id +
                '}';
    }
}
