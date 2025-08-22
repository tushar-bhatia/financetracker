export interface FinanceSummary {
  income: number,
  expense: number,
  balance: number,
  categoryExpense: Map<string, number>
}
