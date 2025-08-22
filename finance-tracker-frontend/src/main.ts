import { bootstrapApplication } from '@angular/platform-browser';
import { financeConfig } from './app/component/finance.config';
import { Finance } from './app/component/finance';

bootstrapApplication(Finance, financeConfig)
  .catch((err) => console.error(err));
