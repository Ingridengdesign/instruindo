


import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog'; 
import { Observable, of } from 'rxjs';


import { AuthService } from 'src/app/service/auth-service.service';
import { HistoricoService } from 'src/app/service/historico.service';
import { AvaliacaoModalComponent } from '../avaliacao-modal/avaliacao-modal.component';

@Component({
  selector: 'app-historico-tab',
  templateUrl: './historico-tab.component.html',
  styleUrls: ['./historico-tab.component.scss']
})
export class HistoricoTabComponent implements OnInit {

  public historicoAulas: any[] = [];
  public isLoadingHistorico: boolean = true;

  
  public userRole: string = 'DESCONHECIDO';

  constructor(
    private historicoService: HistoricoService,
    public dialog: MatDialog,
    
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    
    this.userRole = this.authService.getRole();
    this.carregarHistorico();
  }

  carregarHistorico(): void {
    let obsHistorico: Observable<any>;
    
    if (this.userRole === 'ROLE_PROFESSOR') {
      obsHistorico = this.historicoService.getHistoricoProfessor();
    } else if (this.userRole === 'ROLE_ALUNO') {
      obsHistorico = this.historicoService.getHistoricoAluno();
    } else {
      obsHistorico = of([]);
    }

    this.isLoadingHistorico = true;
    obsHistorico.subscribe(
      (response: any[]) => {
        this.historicoAulas = response.map((aula: any) => this.formatarDatasArray(aula.dataHoraAula, aula, 'dataHoraAula'));
        this.isLoadingHistorico = false;
      },
      (error: any) => {
        console.error('Erro ao buscar histórico:', error);
        this.isLoadingHistorico = false;
      }
    );
  }

  
  abrirModalAvaliacao(aula: any): void {
    
    const dialogRef = this.dialog.open(AvaliacaoModalComponent, {
      width: '450px',
      data: { idAula: aula.idAula, role: this.userRole } 
    });

    dialogRef.afterClosed().subscribe(resultado => {
      
      if (resultado && resultado.sucesso) {

        
        

        
        const index = this.historicoAulas.findIndex(a => a.idAula === resultado.idAula);

        if (index !== -1) {
          
          if (resultado.role === 'ROLE_ALUNO') {
            this.historicoAulas[index].avaliacaoAluno = resultado.avaliacao;
          } else if (resultado.role === 'ROLE_PROFESSOR') {
            this.historicoAulas[index].avaliacaoProfessor = resultado.avaliacao;
          }

          
          this.historicoAulas = [...this.historicoAulas];
        }
      }
    });
  }

  
  private formatarDatasArray(
    dataValor: any, objetoInteiro: any, campoData: string,
    dataValorFim: any = null, campoDataFim: string = 'dataHoraFim'
  ): any {
    let dataLimpa = null;
    let dataFimLimpa = null;
    if (Array.isArray(dataValor) && dataValor.length >= 5) {
      const d = new Date(dataValor[0], dataValor[1] - 1, dataValor[2], dataValor[3], dataValor[4], (dataValor[5] || 0));
      if (!isNaN(d.getTime())) { dataLimpa = d.toISOString(); }
    } else if (typeof dataValor === 'string') { dataLimpa = dataValor; }

    if (dataValorFim) {
      if (Array.isArray(dataValorFim) && dataValorFim.length >= 5) {
        const d = new Date(dataValorFim[0], dataValorFim[1] - 1, dataValorFim[2], dataValorFim[3], dataValorFim[4], (dataValorFim[5] || 0));
        if (!isNaN(d.getTime())) { dataFimLimpa = d.toISOString(); }
      } else if (typeof dataValorFim === 'string') { dataFimLimpa = dataValorFim; }
    }
    return {
      ...objetoInteiro,
      [campoData]: dataLimpa,
      [campoDataFim]: dataFimLimpa ? dataFimLimpa : objetoInteiro[campoDataFim]
    };
  }
}
