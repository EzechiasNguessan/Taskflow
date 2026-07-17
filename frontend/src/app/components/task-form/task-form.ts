import { Component, input, output, effect } from '@angular/core';
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

  readonly taskToEdit = input<Task | null>(null);
  readonly projectId = input.required<number>();
  readonly taskCreated = output<Task>();
  readonly taskUpdated = output<Task>();

  taskForm = new FormGroup({
    title: new FormControl('', [Validators.required, Validators.maxLength(100)]),
    description: new FormControl(''),
    dueDate: new FormControl('')
  });

  constructor(private taskService: TaskService) {
    effect(() => {
      const task = this.taskToEdit();
      if (task) {
        this.taskForm.patchValue({
          title: task.title,
          description: task.description ?? '',
          dueDate: task.dueDate ?? ''
        });
      } else {
        this.taskForm.reset();
      }
    });
  }

  onSubmit(): void {
    if (this.taskForm.invalid) {
      return;
    }

    const formValue = this.taskForm.value;
    const currentTask = this.taskToEdit();

    if (currentTask) {
      this.taskService.updateTask(currentTask.id, {
        title: formValue.title!,
        description: formValue.description || undefined,
        dueDate: formValue.dueDate || undefined,
        status: currentTask.status,
        projectId: this.projectId()
      }).subscribe({
        next: (updatedTask) => {
          this.taskUpdated.emit(updatedTask);
          this.taskForm.reset();
        },
        error: (err) => console.error('Erreur lors de la modification', err)
      });
    } else {
      this.taskService.createTask({
        title: formValue.title!,
        description: formValue.description || undefined,
        dueDate: formValue.dueDate || undefined,
        projectId: this.projectId()
      }).subscribe({
        next: (createdTask) => {
          this.taskCreated.emit(createdTask);
          this.taskForm.reset();
        },
        error: (err) => console.error('Erreur lors de la création', err)
      });
    }
  }
}