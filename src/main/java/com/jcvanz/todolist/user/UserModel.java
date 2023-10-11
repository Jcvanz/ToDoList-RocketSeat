package com.jcvanz.todolist.user;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Data;

@Data
@Entity(name = "tb_users")

public class UserModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    
    // atributo unique para bloquear a criação de usuários idênticos
    @Column(unique = true)
    
    private String username;
    private String name;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;
}