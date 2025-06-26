package com.ecom.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_txn_dtl")
public class User {
    @Id
    private String userId;

    private String username;
    private String email;
    private String password;
    private String role;

    // Getters and setters...

    @PrePersist
    public void generateUserId() {
        this.userId = UUID.randomUUID().toString();
    }
}

