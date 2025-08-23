import {Component, OnInit, HostListener} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TransactionService } from '../../service/transaction.service';
import { CategoryService } from '../../service/category.service';
import { Transaction } from '../../model/transaction.model';
import { TransactionRequest } from '../../model/transactionRequest.model';
import { TransactionModal } from '../dailog/transaction-modal';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { Category } from '../../model/category.model';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, NgOptimizedImage, MatIconButton, MatIcon, TransactionModal],
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

  // filters
  fromDate: string = '';
  toDate: string = '';
  selectedType: string = '';
  selectedCategory!: Category;
  allCategoryOption: Category = { id: 0, name: 'All Category' };

  // modal parameters
  isVisible: boolean = false;
  modalMode: 'create' | 'edit' | null = 'create';
  selectedTransaction: Transaction = {
    id: null,
    transactionDate: '',
    amount: null,
    category: { id: null, name: '' },
    transactionType: '',
    description: ''
  };

  constructor(private transactionService: TransactionService,
              private categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    let transactionRequest: TransactionRequest = {
      id: null,
      categoryId: null,
      transactionType: null,
      startDate: null,
      endDate: null,
      transactionDate: null,
      amount: null,
      description: null,
    };
    this.loadTransactions(transactionRequest);
  }

  loadTransactions(request: TransactionRequest) {
    this.loading = true;
    this.categoryService.getAllCategories().subscribe({
      next: (data) => {
        this.categoryData = data;
        this.selectedCategory = this.allCategoryOption ;
      },
      error: (err) => {
        throw new Error("Error fetching categories:" + err.message);
      },
    });
    this.transactionService.getAllTransactions(request).subscribe({
      next: (data) => {
        this.transactions = data;
        this.applyFilters();
        this.loading = false;
      },
      error: (err) => {
        throw new Error("Error fetching transactions:" + err.message);
      },
    });
  }

  applyFilters() {
    this.filteredTransactions = this.transactions.filter((tx) => {
      const matchesCategory =
        this.selectedCategory.id === 0 || tx.category?.id === this.selectedCategory.id;

      const matchesDate =
        (!this.fromDate || new Date(tx.transactionDate) >= new Date(this.fromDate)) &&
        (!this.toDate || new Date(tx.transactionDate) <= new Date(this.toDate));

      const matchesType =
        !this.selectedType || tx.transactionType === this.selectedType;

      return matchesCategory && matchesDate && matchesType;
    });
  }

  onCreate() {
    console.log("Creating new transaction");
    let newTransaction: Transaction = {
      id: null,
      transactionDate: '',
      amount: null,
      category: { id: null, name: '' },
      transactionType: '',
      description: ''
    };
    this.openDialog(newTransaction, 'create');
  }

  onEdit(tx: Transaction) {
    console.log("Editing transaction:", tx);
    let transactionCopy: Transaction = structuredClone<Transaction>(tx)
    this.openDialog(transactionCopy, 'edit');
  }

  onDelete(tx: Transaction) {
    if (confirm(`Are you sure you want to delete transaction: "${tx.description}"?`)) {
      this.transactionService.deleteTransaction(tx.id).subscribe({
        next: (message) => {
          alert(message);
          this.transactions = this.transactions.filter(t => t.id !== tx.id);
          this.applyFilters();
        },
        error: (err) => {
          throw new Error(err.message);
        }
      });
    }
  }

  get totalIncome() {
    return this.filteredTransactions
      .filter((t) => t.transactionType === 'INCOME')
      .reduce((sum, t) => sum + (t.amount ?? 0), 0);
  }

  get totalExpense() {
    return this.filteredTransactions
      .filter((t) => t.transactionType === 'EXPENSE')
      .reduce((sum, t) => sum + (t.amount?? 0), 0);
  }

  get balance() {
    return this.totalIncome - this.totalExpense;
  }

  updateTransaction(tx: Transaction) {
    let transactionRequest: TransactionRequest = {
      id: tx.id,
      categoryId: tx.category.id,
      transactionType: tx.transactionType,
      startDate: null,
      endDate: null,
      transactionDate: tx.transactionDate,
      amount: tx.amount,
      description: tx.description
    };
    this.transactionService.updateTransaction(transactionRequest).subscribe({
      next: (updatedTx) => {
        const index = this.transactions.findIndex(t => t.id === updatedTx.id);
        if (index !== -1) {
          this.transactions[index] = updatedTx;
          alert("Transaction updated successfully!");
          this.applyFilters();
        }
      },
      error: (err) => {
        throw new Error("Error updating transaction:" + err.message);
      }
    });
  }

  createTransaction(tx: Transaction) {
    let transactionRequest: TransactionRequest = {
      id: tx.id,
      categoryId: tx.category.id,
      transactionType: tx.transactionType,
      startDate: null,
      endDate: null,
      transactionDate: tx.transactionDate,
      amount: tx.amount,
      description: tx.description
    };
    this.transactionService.createTransaction(transactionRequest).subscribe({
      next: (newTransaction) => {
        this.transactions = [newTransaction, ...this.transactions];
        alert("Transaction created successfully!");
        this.applyFilters();
      },
      error: (err) => {
        throw new Error("Error adding new transaction:" + err.message);
      }
    });
  }

  onUserAction(tx: Transaction) {
    if(this.modalMode === 'create') {
      this.closeDialog();
      this.createTransaction(tx);
    } else if(this.modalMode === 'edit') {
      this.closeDialog();
      this.updateTransaction(tx);
    } else {
      this.closeDialog();
      throw new Error("Unknow action defined!");
    }
  }

  closeDialog() {
    this.isVisible = false;
    this.selectedTransaction = {
      id: null,
      transactionDate: '',
      amount: null,
      category: { id: null, name: '' },
      transactionType: '',
      description: ''
    };
    this.modalMode = null;
  }

  openDialog(tx: Transaction, mode: 'create' | 'edit') {
    this.modalMode = mode;
    this.selectedTransaction = tx;
    this.isVisible = true;
  }

  onViewAnalysis() {

  }

  onDownloadReport() {
    let transactionRequest: TransactionRequest = {
      id: null,
      categoryId: this.selectedCategory.id===0 ? null : this.selectedCategory.id,
      transactionType: this.selectedType==='' ? null : this.selectedType,
      startDate: this.fromDate==='' ? null : this.fromDate,
      endDate: this.toDate==='' ? null : this.toDate,
      transactionDate: null,
      amount: null,
      description: null,
    }
    this.transactionService.getFinanceReport(transactionRequest).subscribe({
      next: (response) => {
        let header = response.headers.get('Content-Disposition') || 'attachment; filename=finance_report.xlsx';
        let filenameMatch = header.match(/filename="?([^"]+)"?/);
        let filename = filenameMatch ? filenameMatch[1] : 'finance_report.xlsx';
        this.downloadReport(response.body, filename);
      },
      error: (err) => {
        throw new Error("Error generating the transaction report:" + err.message);
      }
    });
  }

  downloadReport(blob: Blob|null, filename: string) {
    if (!blob) {
      alert("No data available for the report generation.");
      return;
    }
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }
}

