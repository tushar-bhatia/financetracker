import { bootstrapApplication } from '@angular/platform-browser';
import { financeConfig } from './app/component/finance/finance.config';
import { Finance } from './app/component/finance/finance';

bootstrapApplication(Finance, financeConfig)
  .catch((err) => console.error(err));
