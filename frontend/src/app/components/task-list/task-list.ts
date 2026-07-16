import { Component, OnInit, signal } from '@angular/core';
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

  constructor(private taskService: TaskService) { }

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getAllTasks().subscribe({
      next: (data) => {
        this.tasks.set(data);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des tâches', err);
      }
    });
  }

  toggleForm(): void {
    this.showForm.update(value => !value);
  }

  onTaskCreated(newTask: Task): void {
    this.tasks.update(currentTasks => [...currentTasks, newTask]);
    this.showForm.set(false);
  }
}