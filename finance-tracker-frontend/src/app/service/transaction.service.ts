import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaction } from '../model/transaction.model';
import { TransactionRequest } from '../model/transactionRequest.model';
import { FinanceSummary } from '../model/financeSummary.model';
import { environment } from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  // 🔹 Change this to your backend's actual API endpoint
  private apiUrl = `${environment.protocol}://${environment.host}:${environment.port}/${environment.transactionApiBaseUrl}`;

  constructor(private http: HttpClient) {}

  /**
   * Fetch all transactions from backend
   */
  getAllTransactions(transactionRequest: TransactionRequest): Observable<Transaction[]> {
   return this.http.post<Transaction[]>(`${this.apiUrl}/${environment.getAll}`, transactionRequest);
  }

  /**
   * Optional: Get single transaction by ID
   */
  getTransactionById(id: number): Observable<Transaction> {
    return this.http.get<Transaction>(`${this.apiUrl}/${environment.getById}/${id}`);
  }

  /**
   * Optional: Create a new transaction
   */
  createTransaction(transactionRequest: TransactionRequest): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.apiUrl}/${environment.create}`, transactionRequest);
  }

  /**
   * Optional: Update existing transaction
   */
  updateTransaction(transactionRequest: TransactionRequest): Observable<Transaction> {
    return this.http.put<Transaction>(`${this.apiUrl}/${environment.update}`, transactionRequest);
  }

  /**
   * Optional: Delete transaction
   */
  deleteTransaction(id: number | null): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${environment.delete}/${id}`, { responseType: 'text' as 'json' });
  }

  /**
   * Optional: Get financial summary of your transactions
   */
  getFinancialSummary(transactionRequest: TransactionRequest): Observable<Map<string, FinanceSummary>> {
    return this.http.post<Map<string, FinanceSummary>>(`${this.apiUrl}/${environment.summary}`, transactionRequest);
  }

  /**
   * Optional: Get report of your finances
   */
  getFinanceReport(transactionRequest: TransactionRequest): Observable<HttpResponse<Blob>> {
    return this.http.post<Blob>(`${this.apiUrl}/${environment.report}`, transactionRequest, {responseType: 'blob' as 'json', observe: 'response' });
  }
}
