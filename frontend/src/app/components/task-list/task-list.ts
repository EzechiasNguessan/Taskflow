import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task.model';
import { TaskForm } from '../task-form/task-form';

@Component({
  selector: 'app-task-list',
  imports: [TaskForm],
  templateUrl: './task-list.html',
  styleUrl: './task-list.css',
})
export class TaskList implements OnInit {

  protected readonly tasks = signal<Task[]>([]);
  protected readonly showForm = signal(false);
  protected readonly taskBeingEdited = signal<Task | null>(null);
  protected projectId!: number;

  constructor(
    private taskService: TaskService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.projectId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getAllTasks().subscribe({
      next: (data) => {
        this.tasks.set(data.filter(t => t.project.id === this.projectId));
      },
      error: (err) => console.error('Erreur lors du chargement des tâches', err)
    });
  }

  goBackToProjects(): void {
    this.router.navigate(['/projects']);
  }

  openCreateForm(): void {
    this.taskBeingEdited.set(null);
    this.showForm.set(true);
  }

  openEditForm(task: Task): void {
    this.taskBeingEdited.set(task);
    this.showForm.set(true);
  }

  closeForm(): void {
    this.showForm.set(false);
    this.taskBeingEdited.set(null);
  }

  onTaskCreated(newTask: Task): void {
    this.tasks.update(current => [...current, newTask]);
    this.closeForm();
  }

  onTaskUpdated(updatedTask: Task): void {
    this.tasks.update(current =>
      current.map(t => t.id === updatedTask.id ? updatedTask : t)
    );
    this.closeForm();
  }

  deleteTask(id: number): void {
    if (!confirm('Es-tu sûr de vouloir supprimer cette tâche ?')) {
      return;
    }

    this.taskService.deleteTask(id).subscribe({
      next: () => {
        this.tasks.update(current => current.filter(t => t.id !== id));
      },
      error: (err) => console.error('Erreur lors de la suppression', err)
    });
  }
}