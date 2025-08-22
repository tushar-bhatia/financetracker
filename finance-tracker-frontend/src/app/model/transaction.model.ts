import { Category } from './category.model';

export interface Transaction {
  id: number,
  transactionDate: string;
  amount: number;
  category: Category;
  transactionType: string;
  description: string;
}
