package com.taskflow.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Task {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @NotBlank(message = "Le titre est obligatoire")
   @Size(max = 200, message = "Le titre ne peut pas dépasser 200 caractères")
   @Column(nullable = false, length = 200)
   private String title;

   @Column(columnDefinition = "TEXT")
   @Size(max = 1000, message = "Mettez une description a votre tâche")
   private String description;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private TaskStatus status = TaskStatus.TODO;

   private LocalDateTime dueDate;

   @CreatedDate
   @Column(updatable = false)
   private LocalDateTime createdAt;

   @LastModifiedDate
   private LocalDateTime updatedAt;
}
