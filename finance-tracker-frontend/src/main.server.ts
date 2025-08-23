// src/main.server.ts
import { bootstrapApplication } from '@angular/platform-browser';
import { Finance } from './app/component/finance/finance';
import { financeConfig } from './app/component/finance/finance.config.server';

const bootstrap = () => bootstrapApplication(Finance, financeConfig);
export default bootstrap;
