import { Component, OnInit } from '@angular/core';
import { MatCalendarCellCssClasses } from '@angular/material/datepicker';
import { AgendaService } from 'src/app/service/agenda.service';
import { SolicitacaoService } from 'src/app/service/solicitacao.service';

@Component({
  selector: 'app-calendario-aulas-tab',
  templateUrl: './calendario-aulas-tab.component.html',
  styleUrls: ['./calendario-aulas-tab.component.scss']
})
export class CalendarioAulasTabComponent implements OnInit {

  public aulasConfirmadas: any[] = [];
  public diasComAulas: Set<string> = new Set<string>();
  public aulasDoDiaSelecionado: any[] = [];
  public isLoadingAgenda: boolean = true;

  constructor(
    private agendaService: AgendaService,
    private solicitacaoService: SolicitacaoService
  ) {  }

  ngOnInit(): void {
    this.carregarAulasConfirmadas();
  }

  carregarAulasConfirmadas(): void {
    this.isLoadingAgenda = true;
    this.agendaService.getAulasConfirmadas().subscribe(
      (aulasResponse: any) => { 
        this.aulasConfirmadas = aulasResponse.map((aula: any) => this.formatarDatasArray(aula.dataHoraAula, aula, 'dataHoraAula'));

        const dias = this.aulasConfirmadas
          .map((aula: any) => aula.dataHoraAula ? aula.dataHoraAula.split('T')[0] : null)
          .filter((d: string | null) => d !== null) as string[];
        this.diasComAulas = new Set(dias);

        this.isLoadingAgenda = false;
      },
      (error: any) => { 
        console.error('Erro ao carregar aulas confirmadas:', error);
        this.isLoadingAgenda = false;
      }
    );
  }

  onDiaSelecionado(data: Date | null): void {
    if (!data) {
      this.aulasDoDiaSelecionado = [];
      return;
    }
    const dataClicada = this.formatarData(data);
    this.aulasDoDiaSelecionado = this.aulasConfirmadas.filter((aula: any) => {
      if (!aula.dataHoraAula) return false;
      return aula.dataHoraAula.startsWith(dataClicada);
    });
  }

  onCancelarAula(idAgendamento: number): void {
    console.log('Tentando cancelar aula com ID:', idAgendamento);

    this.solicitacaoService.cancelarAgendamento(idAgendamento).subscribe(
      (response: any) => {
        console.log('Aula cancelada com sucesso', response);
        this.aulasDoDiaSelecionado = [];
        this.carregarAulasConfirmadas();

      },
      (error: any) => {
        console.error('Erro ao cancelar aula:', error);
      }
    );
  }

  dateClassAulas = (date: Date): MatCalendarCellCssClasses => {
    const dataString = this.formatarData(date);
    if (this.diasComAulas.has(dataString)) {
      return 'dia-com-aula';
    }
    return '';
  }

  private formatarData(date: Date): string {
    if (!date) { return ''; }
    const y = date.getFullYear();
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    return `${y}-${m}-${d}`;
  }

  private formatarDatasArray(dataValor: any, objetoInteiro: any, campoData: string): any {
    let dataLimpa = null;
    if (Array.isArray(dataValor) && dataValor.length >= 5) {
      const d = new Date(dataValor[0], dataValor[1] - 1, dataValor[2], dataValor[3], dataValor[4], (dataValor[5] || 0));
      if (!isNaN(d.getTime())) { dataLimpa = d.toISOString(); }
    } else if (typeof dataValor === 'string') {
      dataLimpa = dataValor;
    }
    return {
      ...objetoInteiro,
      [campoData]: dataLimpa
    };
  }
}
