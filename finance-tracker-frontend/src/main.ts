import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/component/root/app.config';
import { AppComponent } from './app/component/root/app.component';

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
