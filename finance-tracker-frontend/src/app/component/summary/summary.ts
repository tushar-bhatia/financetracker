import { Component, OnInit } from '@angular/core';
import { ChartData, ChartOptions, ChartType } from 'chart.js';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { FinanceSummary } from '../../model/financeSummary.model';
import { BaseChartDirective } from 'ng2-charts';
import { Router } from '@angular/router';

@Component({
  selector: 'finance-summary',
  templateUrl: './summary.html',
  imports: [CommonModule, BaseChartDirective, CurrencyPipe],
  styleUrls: ['./summary.css']
})
export class TransactionSummary implements OnInit {
  summary!: Map<string, FinanceSummary>;

  months: string[] = [];
  selectedMonth: string | null = null;

  // Chart Types
  incomeExpenseChartType: ChartType = 'bar';
  balanceChartType: ChartType = 'line';
  categoryChartType: ChartType = 'pie';

  // Chart Data
  incomeExpenseData: ChartData<'bar'> = { labels: [], datasets: [] };
  balanceData: ChartData<'line'> = { labels: [], datasets: [] };
  categoryData: ChartData<'pie'> = { labels: [], datasets: [] };

  chartOptions: ChartOptions = {
    responsive: true,
    plugins: {
      legend: { position: 'top' },
    }
  };

  constructor(private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras.state && navigation.extras.state['data']) {
      let data = navigation.extras.state['data'] as Map<string, FinanceSummary>;
      this.summary = new Map(Object.entries(data));
    }
  }

  ngOnInit() {
    if (!this.summary) return;

    this.months = Array.from(this.summary.keys()).sort();

    // Income vs Expense (Bar)
    this.incomeExpenseData = {
      labels: this.months,
      datasets: [
        { data: this.months.map(m => this.summary.get(m)!.income), label: 'Income', backgroundColor: '#16a34a' },
        { data: this.months.map(m => this.summary.get(m)!.expense), label: 'Expense', backgroundColor: '#dc2626' }
      ]
    };

    // Balance Growth (Line)
    this.balanceData = {
      labels: this.months,
      datasets: [
        {
          data: this.months.map(m => this.summary.get(m)!.balance),
          label: 'Balance',
          fill: true,
          borderColor: '#2563eb',
          backgroundColor: 'rgba(37, 99, 235, 0.2)',
          tension: 0.3
        }
      ]
    };
  }

  // ✅ Open Category Pie Chart
  openCategoryChart(month: string) {
    this.selectedMonth = month;
    const cat = this.summary.get(month)!.categoryExpense;

    this.categoryData = {
      labels: Object.keys(cat),
      datasets: [
        {
          data: Object.values(cat),
          backgroundColor: ['#f87171', '#facc15', '#4ade80', '#60a5fa', '#a78bfa', '#fb923c'],
          label: 'Category Expenses'
        }
      ]
    };
  }

  // ✅ Close Modal
  closeCategoryChart() {
    this.selectedMonth = null;
  }
}
