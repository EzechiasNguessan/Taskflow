package com.taskflow.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateTaskRequest {

   @NotBlank(message = "Le titre est obligatoire")
   @Size(max = 100, message = "Le titre ne peut pas dépasser 100 caractères")
   private String title;

   private String description;

   private LocalDateTime dueDate;

   @NotNull(message = "Le statut est obligatoire")
   private com.taskflow.backend.entity.TaskStatus status;

   @NotNull(message = "Le projet est obligatoire")
   private Long projectId;
}