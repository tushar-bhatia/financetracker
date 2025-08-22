import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { environment } from '../../environment/environment';
import { Category } from '../model/category.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private apiUrl = `${environment.protocol}://${environment.host}:${environment.port}/${environment.categoryApiBaseUrl}`;

  constructor(private http: HttpClient) {}

  /**
   * Fetch all categories from backend
   */
  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}/${environment.getAll}`);
  }

  /**
   * Get single category by ID
   */
  getCategoryById(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/${environment.getById}/${id}`);
  }

  /**
   * Create a new category
   */
  createCategory(name: string): Observable<Category> {
    let params = new HttpParams().set('name', name);
    return this.http.post<Category>(`${this.apiUrl}/${environment.create}`, params);
  }

  /**
   * Update existing category
   */
  updateCategory(category: Category): Observable<Category> {
    return this.http.put<Category>(`${this.apiUrl}/${environment.update}`, category);
  }

  /**
   * Delete category
   */
  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${environment.delete}/${id}`);
  }
}
