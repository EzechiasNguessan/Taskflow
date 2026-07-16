import { Component, output } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-form',
  imports: [ReactiveFormsModule],
  templateUrl: './task-form.html',
  styleUrl: './task-form.css',
})
export class TaskForm {

  readonly taskCreated = output<Task>();

  taskForm = new FormGroup({
    title: new FormControl('', [Validators.required, Validators.maxLength(100)]),
    description: new FormControl(''),
    dueDate: new FormControl('')
  });

  constructor(private taskService: TaskService) { }

  onSubmit(): void {
    if (this.taskForm.invalid) {
      return;
    }

    const formValue = this.taskForm.value;

    this.taskService.createTask({
      title: formValue.title!,
      description: formValue.description || undefined,
      dueDate: formValue.dueDate || undefined
    }).subscribe({
      next: (createdTask) => {
        this.taskCreated.emit(createdTask);
        this.taskForm.reset();
      },
      error: (err) => {
        console.error('Erreur lors de la création de la tâche', err);
      }
    });
  }
}