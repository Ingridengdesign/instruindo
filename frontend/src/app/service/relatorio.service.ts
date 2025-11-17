

import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RelatorioService {

  private baseUrl = 'http://localhost:8080/api/relatorios/professor';

  constructor(private http: HttpClient) { }


  getRelatorioAgendamentos(dataInicio: string, dataFim: string): Observable<any> {
    let params = new HttpParams()
      .set('inicio', dataInicio)
      .set('fim', dataFim);

    return this.http.get(`${this.baseUrl}/agendamentos`, { params: params });
  }

  getDashboardStats(): Observable<any> {
    const url = `${this.baseUrl}/dashboard-stats`;

    return this.http.get(url);
  }
}
