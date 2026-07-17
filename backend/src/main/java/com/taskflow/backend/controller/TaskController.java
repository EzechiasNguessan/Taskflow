package com.taskflow.backend.controller;

import com.taskflow.backend.dto.CreateTaskRequest;
import com.taskflow.backend.dto.UpdateTaskRequest;
import com.taskflow.backend.entity.Task;
import com.taskflow.backend.entity.TaskHistory;
import com.taskflow.backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

   private final TaskService taskService;

   public TaskController(TaskService taskService) {
      this.taskService = taskService;
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public Task createTask(@Valid @RequestBody CreateTaskRequest request) {
      return taskService.createTask(request);
   }

   @GetMapping
   public List<Task> getAllTasks() {
      return taskService.getAllTasks();
   }

   @GetMapping("/{id}")
   public Task getTaskById(@PathVariable Long id) {
      return taskService.getTaskById(id);
   }

   @PutMapping("/{id}")
   public Task updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
      return taskService.updateTask(id, request);
   }

   @DeleteMapping("/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void deleteTask(@PathVariable Long id) {
      taskService.deleteTask(id);
   }

   @GetMapping("/{id}/history")
   public List<TaskHistory> getTaskHistory(@PathVariable Long id) {
      return taskService.getTaskHistory(id);
   }
}