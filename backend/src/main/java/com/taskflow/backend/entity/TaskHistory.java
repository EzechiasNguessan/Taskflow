package com.taskflow.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskHistory {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "task_id", nullable = false)
   private Task task;

   @Column(nullable = false, length = 50)
   private String fieldName;

   @Column(columnDefinition = "TEXT")
   private String oldValue;

   @Column(columnDefinition = "TEXT")
   private String newValue;

   @Column(nullable = false)
   private LocalDateTime changedAt;
}