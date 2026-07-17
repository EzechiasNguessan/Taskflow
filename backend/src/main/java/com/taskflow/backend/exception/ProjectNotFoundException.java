package com.taskflow.backend.exception;

public class ProjectNotFoundException extends RuntimeException {

   public ProjectNotFoundException(Long id) {
      super("Aucun projet trouvé avec l'id : " + id);
   }
}