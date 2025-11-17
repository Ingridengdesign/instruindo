

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {

  private baseUrl = 'http://localhost:8080/api/public/categorias';

  constructor(private http: HttpClient) { }


  getCategorias(): Observable<any[]> {

    return this.http.get<any[]>(this.baseUrl);
  }
}
