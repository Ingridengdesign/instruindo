

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AvaliacaoService {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  
  avaliarAulaAluno(idAula: number, avaliacao: any): Observable<any> {
    const url = `${this.baseUrl}/aulas/${idAula}/avaliacoes`;

    
    
    return this.http.post(url, avaliacao, { responseType: 'text' });
  }

  
  avaliarAulaProfessor(idAula: number, avaliacao: any): Observable<any> {
    const url = `${this.baseUrl}/aulas/${idAula}/avaliacoes/professor`;

    
    return this.http.post(url, avaliacao, { responseType: 'text' });
  }
}
