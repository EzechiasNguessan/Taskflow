package com.taskflow.backend.service;

import com.taskflow.backend.dto.CreateTaskRequest;
import com.taskflow.backend.dto.UpdateTaskRequest;
import com.taskflow.backend.entity.Project;
import com.taskflow.backend.entity.Task;
import com.taskflow.backend.entity.TaskHistory;
import com.taskflow.backend.exception.ProjectNotFoundException;
import com.taskflow.backend.exception.TaskNotFoundException;
import com.taskflow.backend.repository.ProjectRepository;
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
   private final ProjectRepository projectRepository;

   public TaskService(TaskRepository taskRepository,
         TaskHistoryRepository taskHistoryRepository,
         ProjectRepository projectRepository) {
      this.taskRepository = taskRepository;
      this.taskHistoryRepository = taskHistoryRepository;
      this.projectRepository = projectRepository;
   }

   public Task createTask(CreateTaskRequest request) {
      Project project = projectRepository.findById(request.getProjectId())
            .orElseThrow(() -> new ProjectNotFoundException(request.getProjectId()));

      Task task = new Task();
      task.setTitle(request.getTitle());
      task.setDescription(request.getDescription());
      task.setDueDate(request.getDueDate());
      task.setProject(project);

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

   public Task updateTask(Long id, UpdateTaskRequest request) {
      Task existingTask = getTaskById(id);

      if (!Objects.equals(existingTask.getTitle(), request.getTitle())) {
         recordHistory(existingTask, "title", existingTask.getTitle(), request.getTitle());
         existingTask.setTitle(request.getTitle());
      }

      if (existingTask.getStatus() != request.getStatus()) {
         recordHistory(existingTask, "status", existingTask.getStatus().name(), request.getStatus().name());
         existingTask.setStatus(request.getStatus());
      }

      if (!Objects.equals(existingTask.getProject().getId(), request.getProjectId())) {
         Project newProject = projectRepository.findById(request.getProjectId())
               .orElseThrow(() -> new ProjectNotFoundException(request.getProjectId()));
         existingTask.setProject(newProject);
      }

      existingTask.setDescription(request.getDescription());
      existingTask.setDueDate(request.getDueDate());

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