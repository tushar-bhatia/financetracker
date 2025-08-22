import {Category} from './category.model';

export interface TransactionRequest {
  id: number | null,
  categoryId: number | null,
  transactionType: string | null,
  startDate: string | null,
  endDate: string | null,
  transactionDate: string | null,
  amount: number | null,
  description: string | null;
}
