package com.taskflow.backend.exception;

public class TaskNotFoundException extends RuntimeException {

   public TaskNotFoundException(Long id) {
      super("Aucune tâche trouvée avec l'id : " + id);
   }
}