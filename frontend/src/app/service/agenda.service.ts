

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AgendaService {

  private baseUrl = 'http://localhost:8080/api/agenda/professor';

  constructor(private http: HttpClient) { }


  getDisponibilidade(): Observable<any> {

    return this.http.get(`${this.baseUrl}/disponibilidade`);
  }


  atualizarDisponibilidade(disponibilidade: any): Observable<any> {

    return this.http.put(
      `${this.baseUrl}/disponibilidade`,
      disponibilidade,
      { responseType: 'text' }
    );
  }


  getBloqueios(): Observable<any> {
    return this.http.get(`${this.baseUrl}/bloqueios`);
  }


  criarBloqueio(bloqueio: any): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/bloqueios`,
      bloqueio,
      { responseType: 'text' }
    );
  }


  apagarBloqueio(id: number): Observable<any> {
    return this.http.delete(
      `${this.baseUrl}/bloqueios/${id}`,
      { responseType: 'text' }
    );
  }

  getAulasConfirmadas(): Observable<any> {
    return this.http.get(`${this.baseUrl}/aulas`);
  }

  cancelarAula(idAgendamento: number): Observable<any> {
    
    return this.http.delete(
      `${this.baseUrl}/aulas/${idAgendamento}`,
      { responseType: 'text' }
    );
  }

}
