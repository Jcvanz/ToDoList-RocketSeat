package com.jcvanz.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
// nome da tabela
@Entity(name = "tb_tasks")
public class TaskModel {
    // chave primaria
    @Id

    // gera o id automaticamente
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;

    // limita o número de caracter do title
    @Column(length = 50)
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    private UUID idUser;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    // verificação do tamanho do título
    public void setTitle(String title) throws Exception {
        // faz a verificação
        if(title.length() > 50) {
            throw new Exception("O campo título deve conter no máximo 50 caracteres");
        }
        this.title = title;
    }
}
