import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../service/transaction.service';
import { Transaction } from '../model/transaction.model';
import { TransactionRequest } from '../model/transactionRequest.model';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './finance.html',
  styleUrls: ['./finance.css'],
})
export class Finance implements OnInit {
  transactions: Transaction[] = [];
  filteredTransactions: Transaction[] = [];
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
  q: string = '';
  fromMonth: string = '';
  toMonth: string = '';

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    this.loadTransactions(this.transactionRequest);
  }

  loadTransactions(request: TransactionRequest) {
    this.loading = true;
    this.transactionService.getAllTransactions(request).subscribe({
      next: (data) => {
        this.transactions = data;
        //this.applyFilters();
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
      const query = this.q.toLowerCase();
      const matchesQuery =
        !this.q ||
        tx.description.toLowerCase().includes(query) ||
        tx.category?.name.toLowerCase().includes(query);

      const txDate = new Date(tx.transactionDate);
      const from = this.fromMonth ? new Date(this.fromMonth + '-01') : null;
      const to = this.toMonth
        ? new Date(this.toMonth + '-01')
        : null;

      let matchesDate = true;
      if (from && txDate < from) matchesDate = false;
      if (to) {
        const toEnd = new Date(to.getFullYear(), to.getMonth() + 1, 0);
        if (txDate > toEnd) matchesDate = false;
      }

      return matchesQuery && matchesDate;
    });
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

