import {Component, OnInit, HostListener, ChangeDetectorRef, NgModule, ErrorHandler} from '@angular/core';
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

  constructor(private transactionService: TransactionService,
              private categoryService: CategoryService,
              private cdr: ChangeDetectorRef
  ) {}

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
        this.loading = false;
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
    throw new Error('Method not implemented.');
    // TODO: open edit modal or navigate to edit form
  }

  onDelete(tx: any) {
    if (confirm(`Are you sure you want to delete transaction: "${tx.description}"?`)) {
      this.transactionService.deleteTransaction(tx.id).subscribe({
        next: () => {
          this.transactions = this.transactions.filter(t => t.id !== tx.id);
          this.cdr.detectChanges();
          this.applyFilters();
        },
        error: (err) => {
          console.error('Error deleting transaction', err);
        }
      });
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

  showErrorPopup(message: string): void {
    // Create a popup div dynamically
    const popup = document.createElement('div');
    popup.style.position = 'fixed';
    popup.style.top = '50%';
    popup.style.left = '50%';
    popup.style.transform = 'translate(-50%, -50%)';
    popup.style.backgroundColor = '#f8d7da';
    popup.style.color = '#721c24';
    popup.style.padding = '20px';
    popup.style.border = '1px solid #f5c6cb';
    popup.style.borderRadius = '5px';
    popup.style.zIndex = '1000';
    popup.innerHTML = `
    <h3>Error</h3>
    <p>${message}</p>
    <button onclick="this.parentElement.remove()">Close</button>
    `;
    document.body.appendChild(popup);
  }
}

