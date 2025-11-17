

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SignupService {

  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  cadastrarAluno(formDados: any): Observable<any> {
    const url = `${this.baseUrl}/api/cadastro/aluno`;

    
    
    return this.http.post(url, formDados, { responseType: 'text' });
  }

  cadastrarProfessor(formDados: any): Observable<any> {
    const url = `${this.baseUrl}/api/cadastro/professor`;

    
    return this.http.post(url, formDados, { responseType: 'text' });
  }
}
