package com.taskflow.backend.service;

import com.taskflow.backend.entity.Project;
import com.taskflow.backend.exception.ProjectNotFoundException;
import com.taskflow.backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

   private final ProjectRepository projectRepository;

   public ProjectService(ProjectRepository projectRepository) {
      this.projectRepository = projectRepository;
   }

   public Project createProject(Project project) {
      return projectRepository.save(project);
   }

   public Project getProjectById(Long id) {
      return projectRepository.findById(id)
            .orElseThrow(() -> new ProjectNotFoundException(id));
   }

   public List<Project> getAllProjects() {
      return projectRepository.findAll();
   }

   public Project updateProject(Long id, Project updatedProject) {
      Project existingProject = getProjectById(id);
      existingProject.setName(updatedProject.getName());
      existingProject.setDescription(updatedProject.getDescription());
      return projectRepository.save(existingProject);
   }

   public void deleteProject(Long id) {
      Project project = getProjectById(id);
      projectRepository.delete(project);
   }
}