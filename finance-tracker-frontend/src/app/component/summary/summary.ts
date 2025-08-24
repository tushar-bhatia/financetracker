import { Component, Input, OnInit } from '@angular/core';
import { ChartData, ChartOptions } from 'chart.js';
import { CommonModule } from '@angular/common';
import { FinanceSummary } from '../../model/financeSummary.model';
import { BaseChartDirective } from 'ng2-charts';
import { CurrencyPipe } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'finance-summary',
  templateUrl: './summary.html',
  imports: [CommonModule, BaseChartDirective, CurrencyPipe],
  styleUrls: ['./summary.css']
})
export class TransactionSummary implements OnInit {
  summary!: Map<string, FinanceSummary>;

  constructor(private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras.state && navigation.extras.state['data']) {
      let data = navigation.extras.state['data'] as Map<string, FinanceSummary>;
      this.summary = new Map(Object.entries(data));
      console.log(this.summary);
    }
  }

  months: string[] = [];
  selectedMonth: string | null = null;

  // Charts
  incomeExpenseData: ChartData<'bar'> = { labels: [], datasets: [] };
  balanceData: ChartData<'line'> = { labels: [], datasets: [] };
  categoryData: ChartData<'pie'> = { labels: [], datasets: [] };

  chartOptions: ChartOptions = {
    responsive: true,
    plugins: {
      legend: { position: 'top' },
    }
  };

  ngOnInit() {
    if (!this.summary) return;

    this.months = Array.from(this.summary.keys()).sort();

    // Income vs Expense Bar Chart
    this.incomeExpenseData = {
      labels: this.months,
      datasets: [
        { data: this.months.map(m => this.summary.get(m)!.income), label: 'Income' },
        { data: this.months.map(m => this.summary.get(m)!.expense), label: 'Expense' }
      ]
    };

    // Balance Line Chart
    this.balanceData = {
      labels: this.months,
      datasets: [
        { data: this.months.map(m => this.summary.get(m)!.balance), label: 'Balance', fill: true, borderColor: '#4CAF50', backgroundColor: 'rgba(76, 175, 80, 0.2)' }
      ]
    };
  }

  selectMonth(month: string) {
    this.selectedMonth = month;
    const cat = this.summary.get(month)!.categoryExpense;
    this.categoryData = {
      labels: Object.keys(cat),
      datasets: [
        { data: Object.values(cat), label: 'Category Expenses' }
      ]
    };
  }
}
