import { Component, OnInit, signal } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-list',
  imports: [],
  templateUrl: './task-list.html',
  styleUrl: './task-list.css',
})
export class TaskList implements OnInit {

  protected readonly tasks = signal<Task[]>([]);

  constructor(private taskService: TaskService) { }

  ngOnInit(): void {
    this.taskService.getAllTasks().subscribe({
      next: (data) => {
        this.tasks.set(data);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des tâches', err);
      }
    });
  }
}