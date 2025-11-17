

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SenhaService {

  private baseUrl = 'http://localhost:8080/api/public/senha';

  constructor(private http: HttpClient) { }


  solicitarReset(email: string): Observable<any> {
    const dto = { email: email };

    return this.http.post(`${this.baseUrl}/solicitar-reset`, dto, { responseType: 'text' });
  }


  resetarSenha(dto: any): Observable<any> {

    return this.http.post(`${this.baseUrl}/resetar`, dto, { responseType: 'text' });
  }
}
