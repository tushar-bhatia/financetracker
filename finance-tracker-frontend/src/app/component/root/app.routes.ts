import { Routes } from '@angular/router';
import { Finance } from '../finance/finance';
import { TransactionSummary } from '../summary/summary';

export const routes: Routes = [
  { path: 'dashboard', component: Finance },
  { path: 'summary', component: TransactionSummary },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
];