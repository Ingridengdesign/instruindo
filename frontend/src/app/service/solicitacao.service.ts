import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SolicitacaoService {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }


  criarSolicitacao(dadosSolicitacao: any): Observable<any> {

    const url = `${this.baseUrl}/solicitacoes`;

    return this.http.post(url, dadosSolicitacao, { responseType: 'text' });
  }

  getSolicitacoesProfessor(): Observable<any> {
    const url = `${this.baseUrl}/solicitacoes/professor`;

    return this.http.get(url);
  }

  getSolicitacoesAluno(): Observable<any> {
    return this.http.get(`${this.baseUrl}/solicitacoes/aluno`);
  }

  responderSolicitacao(id: number, acao: 'ACEITAR' | 'RECUSAR', local: string | null = null): Observable<any> {
    const url = `${this.baseUrl}/solicitacoes/${id}/responder`;

    
    const body: any = {
      acao: acao
    };

    if (acao === 'ACEITAR' && local) {
      body.local = local;
    }

    
    return this.http.post(url, body, { responseType: 'text' });
  }

  cancelarAgendamento(idAgendamento: number): Observable<any> {
    const url = `${this.baseUrl}/agendamentos/${idAgendamento}/cancelar`;

    
    return this.http.put(url, {}, { responseType: 'text' });
  }


}
