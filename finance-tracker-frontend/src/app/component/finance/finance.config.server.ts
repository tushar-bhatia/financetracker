// src/app/app.config.server.ts
import { ApplicationConfig } from '@angular/core';
import { provideServerRendering } from '@angular/platform-server';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser'; // ✅

export const financeConfig: ApplicationConfig = {
  providers: [
    provideServerRendering(),
    provideClientHydration(),
    provideClientHydration(withEventReplay())// ✅ enable hydration serialization
  ]
};
