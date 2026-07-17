import { Routes } from '@angular/router';
import { ProjectList } from './components/project-list/project-list';
import { TaskList } from './components/task-list/task-list';

export const routes: Routes = [
  { path: '', redirectTo: 'projects', pathMatch: 'full' },
  { path: 'projects', component: ProjectList },
  { path: 'projects/:id/tasks', component: TaskList },
];