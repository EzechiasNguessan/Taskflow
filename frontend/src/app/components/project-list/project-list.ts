import { Component, OnInit, signal } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { Project } from '../../models/project.model';

@Component({
  selector: 'app-project-list',
  imports: [ReactiveFormsModule],
  templateUrl: './project-list.html',
  styleUrl: './project-list.css',
})
export class ProjectList implements OnInit {

  protected readonly projects = signal<Project[]>([]);
  protected readonly showForm = signal(false);

  projectForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.maxLength(100)]),
    description: new FormControl('')
  });

  constructor(
    private projectService: ProjectService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.projectService.getAllProjects().subscribe({
      next: (data) => this.projects.set(data),
      error: (err) => console.error('Erreur lors du chargement des projets', err)
    });
  }

  toggleForm(): void {
    this.showForm.update(value => !value);
  }

  onSubmit(): void {
    if (this.projectForm.invalid) {
      return;
    }

    const formValue = this.projectForm.value;

    this.projectService.createProject({
      name: formValue.name!,
      description: formValue.description || undefined
    }).subscribe({
      next: (newProject) => {
        this.projects.update(current => [...current, newProject]);
        this.projectForm.reset();
        this.showForm.set(false);
      },
      error: (err) => console.error('Erreur lors de la création du projet', err)
    });
  }

  deleteProject(id: number): void {
    if (!confirm('Supprimer ce projet supprimera aussi toutes ses tâches. Continuer ?')) {
      return;
    }

    this.projectService.deleteProject(id).subscribe({
      next: () => {
        this.projects.update(current => current.filter(p => p.id !== id));
      },
      error: (err) => console.error('Erreur lors de la suppression du projet', err)
    });
  }

  openTasks(projectId: number): void {
    this.router.navigate(['/projects', projectId, 'tasks']);
  }
}