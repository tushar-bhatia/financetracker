import { Component, EventEmitter, Input, Output } from '@angular/core';
import {Transaction} from '../../model/transaction.model';
import {Category} from '../../model/category.model';
import {FormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'transaction-dialog',
  templateUrl: './transaction-modal.html',
  imports: [
    FormsModule,
    NgIf,
    NgForOf
  ],
  styleUrls: ['./transaction-modal.css']
})
export class TransactionModal {
  @Input() visible: boolean = false;  // control modal open/close
  @Input() mode: 'create' | 'edit' | null = 'create';
  @Input() transaction: Transaction = {
    id: null,
    transactionDate: '',
    amount: null,
    category: { id: null, name: '' },
    transactionType: '',
    description: ''
  };
  @Input() categories: Category[] = [];
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<Transaction>();

  onSave() {
    this.save.emit(this.transaction);
  }

  onCancel() {
    this.close.emit();
  }

  compareCategory(c1: Category, c2: Category): boolean {
    return c1 && c2 ? c1.id === c2.id : c1 === c2;
  }
}
