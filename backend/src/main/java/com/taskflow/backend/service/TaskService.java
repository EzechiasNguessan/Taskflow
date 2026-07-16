package com.taskflow.backend.service;

import com.taskflow.backend.entity.Task;
import com.taskflow.backend.entity.TaskHistory;
import com.taskflow.backend.exception.TaskNotFoundException;
import com.taskflow.backend.repository.TaskHistoryRepository;
import com.taskflow.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TaskService {

   private final TaskRepository taskRepository;
   private final TaskHistoryRepository taskHistoryRepository;

   public TaskService(TaskRepository taskRepository, TaskHistoryRepository taskHistoryRepository) {
      this.taskRepository = taskRepository;
      this.taskHistoryRepository = taskHistoryRepository;
   }

   public Task createTask(Task task) {
      Task savedTask = taskRepository.save(task);

      recordHistory(savedTask, "title", null, savedTask.getTitle());
      recordHistory(savedTask, "status", null, savedTask.getStatus().name());

      return savedTask;
   }

   public Task getTaskById(Long id) {
      return taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
   }

   public List<Task> getAllTasks() {
      return taskRepository.findAll();
   }

   public Task updateTask(Long id, Task updatedTask) {
      Task existingTask = getTaskById(id);

      if (!Objects.equals(existingTask.getTitle(), updatedTask.getTitle())) {
         recordHistory(existingTask, "title", existingTask.getTitle(), updatedTask.getTitle());
         existingTask.setTitle(updatedTask.getTitle());
      }

      if (existingTask.getStatus() != updatedTask.getStatus()) {
         recordHistory(existingTask, "status", existingTask.getStatus().name(), updatedTask.getStatus().name());
         existingTask.setStatus(updatedTask.getStatus());
      }

      existingTask.setDescription(updatedTask.getDescription());
      existingTask.setDueDate(updatedTask.getDueDate());

      return taskRepository.save(existingTask);
   }

   public void deleteTask(Long id) {
      Task task = getTaskById(id);
      taskRepository.delete(task);
   }

   public List<TaskHistory> getTaskHistory(Long taskId) {
      return taskHistoryRepository.findByTaskIdOrderByChangedAtDesc(taskId);
   }

   private void recordHistory(Task task, String fieldName, String oldValue, String newValue) {
      TaskHistory history = new TaskHistory();
      history.setTask(task);
      history.setFieldName(fieldName);
      history.setOldValue(oldValue);
      history.setNewValue(newValue);
      history.setChangedAt(LocalDateTime.now());
      taskHistoryRepository.save(history);
   }
}