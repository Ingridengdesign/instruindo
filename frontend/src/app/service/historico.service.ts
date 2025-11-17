

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HistoricoService {

  
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  
  getHistoricoProfessor(): Observable<any> {
    
    return this.http.get(`${this.baseUrl}/historico/professor`);
  }

  
  getHistoricoAluno(): Observable<any> {
    
    return this.http.get(`${this.baseUrl}/historico/aluno`);
  }
}
