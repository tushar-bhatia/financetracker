import {Injectable, ErrorHandler, inject} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogComponent } from './error';
import { ErrorQueueService } from './error-queue-service';

@Injectable({
  providedIn: 'root',
})
export class GlobalErrorHandlerService implements ErrorHandler {

  private errorQueue = inject(ErrorQueueService);

  handleError(error: any): void {
    const message = error?.message || 'Something went wrong!';
    this.errorQueue.addError(message);
    console.error(error);
  }
}
