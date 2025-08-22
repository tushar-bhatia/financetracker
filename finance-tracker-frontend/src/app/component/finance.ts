import { Component, OnInit, HostListener } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../service/transaction.service';
import { CategoryService } from '../service/category.service';
import { Transaction } from '../model/transaction.model';
import { TransactionRequest } from '../model/transactionRequest.model';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { Category } from '../model/category.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, NgOptimizedImage, MatIconButton, MatIcon ],
  templateUrl: './finance.html',
  styleUrls: ['./finance.css'],
})
export class Finance implements OnInit {
  isShrink = false;
  @HostListener('window:scroll', ['$event'])
  onWindowScroll(event: Event) {
    this.isShrink = window.scrollY > 0;
  };
  transactions: Transaction[] = [];
  filteredTransactions: Transaction[] = [];
  categoryData: Category[] = [];
  loading = false;
  transactionRequest: TransactionRequest = {
    id: null,
    categoryId: null,
    transactionType: null,
    startDate: null,
    endDate: null,
    transactionDate: null,
    amount: null,
    description: null,
  };

  // filters
  fromDate: string = '';
  toDate: string = '';
  selectedType: string = '';
  selectedCategory: string = ''

  constructor(private transactionService: TransactionService, private categoryService: CategoryService) {}

  ngOnInit(): void {
    this.loadTransactions(this.transactionRequest);
  }

  loadTransactions(request: TransactionRequest) {
    this.loading = true;
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categoryData = data;
      },
      error: (err) => {
        console.error('Error fetching categories', err);
      },
    });
    this.transactionService.getAllTransactions(request).subscribe({
      next: (data) => {
        this.transactions = data;
        this.applyFilters();
        this.loading = true;
      },
      error: (err) => {
        console.error('Error fetching transactions', err);
        this.loading = false;
      },
    });
  }

  applyFilters() {
    this.filteredTransactions = this.transactions.filter((tx) => {
      const matchesCategory =
        !this.selectedCategory || tx.category?.name === this.selectedCategory;

      const matchesDate =
        (!this.fromDate || new Date(tx.transactionDate) >= new Date(this.fromDate)) &&
        (!this.toDate || new Date(tx.transactionDate) <= new Date(this.toDate));

      const matchesType =
        !this.selectedType || tx.transactionType === this.selectedType;

      return matchesCategory && matchesDate && matchesType;
    });
  }

  onEdit(tx: any) {
    console.log("Editing transaction:", tx);
    // TODO: open edit modal or navigate to edit form
  }

  onDelete(tx: any) {
    if (confirm(`Are you sure you want to delete transaction: "${tx.description}"?`)) {
      console.log("Deleting transaction:", tx);
      // TODO: call delete API and refresh list
    }
  }

  get totalIncome() {
    return this.filteredTransactions
      .filter((t) => t.transactionType === 'INCOME')
      .reduce((sum, t) => sum + t.amount, 0);
  }

  get totalExpense() {
    return this.filteredTransactions
      .filter((t) => t.transactionType === 'EXPENSE')
      .reduce((sum, t) => sum + t.amount, 0);
  }

  get balance() {
    return this.totalIncome - this.totalExpense;
  }
}

