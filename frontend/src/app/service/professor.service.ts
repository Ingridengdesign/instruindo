

import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfessorService {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }


  buscarProfessores(filtros: any): Observable<any> {

    let params = new HttpParams();

    if (filtros.idCategoria) {
      params = params.append('idCategoria', filtros.idCategoria);
    }
    if (filtros.precoMax) {
      params = params.append('precoMax', filtros.precoMax);
    }
    if (filtros.notaMin) {
      params = params.append('notaMin', filtros.notaMin);
    }

    const url = `${this.baseUrl}/professores/buscar`;

    return this.http.get(url, { params: params });
  }


  getProfessorById(id: number): Observable<any> {
    const url = `${this.baseUrl}/professores/${id}`;
    return this.http.get(url);
  }

  getAgendaPublica(id: number, data: string): Observable<any> {

    
    let params = new HttpParams()
      .set('data', data); 

    
    const url = `${this.baseUrl}/professores/${id}/horarios-disponiveis`;

    
    return this.http.get(url, { params: params });
  }

  getDiasDisponiveis(id: number, mes: number, ano: number): Observable<string[]> {

    
    let params = new HttpParams()
      .set('ano', ano.toString())
      .set('mes', mes.toString());

    
    const url = `${this.baseUrl}/professores/${id}/dias-disponiveis`;

    
    
    return this.http.get<string[]>(url, { params: params });
  }

}
