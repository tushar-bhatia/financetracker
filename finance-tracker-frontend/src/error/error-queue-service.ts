// src/app/error-queue.service.ts
import { Injectable, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogComponent } from './error';

@Injectable({ providedIn: 'root' })
export class ErrorQueueService {
  private dialog = inject(MatDialog);
  private queue: string[] = [];
  private isDialogOpen = false;

  addError(message: string) {
    this.queue.push(message);
    this.showNext();
  }

  private showNext() {
    if (this.isDialogOpen || this.queue.length === 0) return;

    const message = this.queue.shift();
    this.isDialogOpen = true;

    const dialogRef = this.dialog.open(ErrorDialogComponent, {
      data: { message },
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(() => {
      this.isDialogOpen = false;
      this.showNext(); // show next error in queue
    });
  }
}
