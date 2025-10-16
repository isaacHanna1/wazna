package com.watad.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "persistent_logins")
public class PersistentLogin {
    @Id
    @Column(length = 64)
    private String series;

    @Column(nullable = false, length = 64 ,name = "username")
    private String username;

    @Column(nullable = false, length = 64)
    private String token;


    @Column(name = "last_used", nullable = false)
    private LocalDateTime lastUsed;


    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }
}
