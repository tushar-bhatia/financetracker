import { Category } from './category.model';

export interface Transaction {
  id: number | null,
  transactionDate: string;
  amount: number | null;
  category: Category;
  transactionType: string;
  description: string;
}
